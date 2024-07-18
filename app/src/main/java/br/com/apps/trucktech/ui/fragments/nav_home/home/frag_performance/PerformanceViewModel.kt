package br.com.apps.trucktech.ui.fragments.nav_home.home.frag_performance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.model.model.travel.PerformanceItem
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.expressions.getDayFormatted
import br.com.apps.trucktech.expressions.getKeyByValue
import br.com.apps.model.expressions.getMonthInPtBrAbbreviated
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.TravelUseCase
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

private const val TRAVEL = "Viagem"
private const val MONTH = "Mês"
private const val YEAR = "Ano"

class PerformanceViewModel(private val useCase: TravelUseCase) : ViewModel() {

    private lateinit var dataSet: List<Travel>
    private lateinit var averageAim: BigDecimal
    private lateinit var performanceAim: BigDecimal

    /**
     * View data:
     * This HashMap contains an Integer that serves as an identifier for the adapter position, and
     * a pair consisting of a String and a list of performance items. The String represents data for the
     * first adapter that displays the period, and the list of items represents data for the ViewPager.
     */
    private val _data = MutableLiveData<HashMap<Int, Pair<String, List<PerformanceItem>>>>()
    val data get() = _data

    private val _state = MutableLiveData<State>()
    val state get() = _state

    /**
     * Header recycler data.
     */
    val headerItemsMap = mapOf(
        Pair(0, TRAVEL),
        Pair(1, MONTH),
        Pair(2, YEAR),
    )

    /**
     * Header adapter position.
     */
    private var _headerPos = 0
    val headerPos get() = _headerPos

    /**
     * Period adapter position.
     */
    private var _periodPos = 0
    val periodPos get() = _periodPos

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    fun initialize(dataSet: List<Travel>, averageAim: BigDecimal, performanceAim: BigDecimal) {
        this@PerformanceViewModel.dataSet = dataSet
        this@PerformanceViewModel.averageAim = averageAim
        this@PerformanceViewModel.performanceAim = performanceAim

        val viewData = filterDataByHeader()

        if (viewData.isNotEmpty()) {
            setState(State.Loaded)
            _data.postValue(viewData)
        } else {
            setState(State.Empty)
        }

    }

    fun setState(state: State) {
        _state.value = state
    }

    /**
     * Updates the selected header position based on the provided header title.
     * @see _headerPos
     * @param headerTitle The title of the header to be selected.
     */
    fun newHeaderSelected(headerTitle: String) {
        val newPos = headerItemsMap.getKeyByValue(headerTitle)!!
        if (_headerPos != newPos) {
            _periodPos = 0
            _headerPos = headerItemsMap.getKeyByValue(headerTitle)!!
            _data.postValue(filterDataByHeader())
        }
    }

    /**
     * Updates the selected period position.
     *
     * @param adapterPos The position of the selected period in the adapter.
     * @see _periodPos
     */
    fun newPeriodSelected(adapterPos: Int) {
        if (_periodPos != adapterPos) {
            _periodPos = adapterPos
        }
    }

    fun getPeriodData(): List<String> {
        val data = (_data.value as HashMap<Int, Pair<String, List<PerformanceItem>>>)
        return data.values.map { it.first }
    }

    /**
     * Retrieves the data for the ViewPager.
     *
     * @return A list of PerformanceItem objects representing the data for the ViewPager.
     */
    fun getPagerData(): List<PerformanceItem> {
        val data = (_data.value as HashMap<Int, Pair<String, List<PerformanceItem>>>)
        return data[periodPos]?.second ?: emptyList()
    }

    /**
     * This method will group travel data according to the criteria defined in the view's header,
     * whether it is for individual, monthly, or annual searches.
     *
     * @return The data for the view.
     * @throws IllegalArgumentException if the header position is invalid.
     */
    private fun filterDataByHeader(): HashMap<Int, Pair<String, List<PerformanceItem>>> {
        return when (headerPos) {
            0 -> getDailyDataSet()
            1 -> getMonthlyDataSet()
            2 -> getYearlyDataSet()
            else -> throw IllegalArgumentException()
        }
    }

    private fun getDailyDataSet(): HashMap<Int, Pair<String, List<PerformanceItem>>> {
        val hashMap = HashMap<Int, Pair<String, List<PerformanceItem>>>()
        val travels = dataSet.sortedByDescending { it.initialDate }
        var mapIndex = 0

        travels.forEachIndexed { index, travel ->
            if (travel.considerAverage) {
                val nextInd = (index + 1)
                val isTheLastOfList = nextInd == travels.size
                val isTheNextOk = nextInd < travels.size && travels[index + 1].considerAverage
                val isTheNextInvalid = nextInd < travels.size && !travels[index + 1].considerAverage

                val subList = when {
                    isTheLastOfList -> listOf(travel)

                    isTheNextOk -> listOf(travel)

                    isTheNextInvalid -> {
                        val partialEndIndex =
                            travels.drop(nextInd).indexOfFirst { it.considerAverage }

                        val endIndex = if (partialEndIndex == -1) {
                            travels.size


                        } else {
                            partialEndIndex + nextInd
                        }
                        travels.subList(index, endIndex)
                    }

                    else -> emptyList()
                }

                val periodAdapterData = "${subList.last().initialDate.getDayFormatted()} " +
                        "${subList.last().initialDate.getMonthInPtBrAbbreviated()} " +
                        "${subList.size}"
                val viewPagerData = getPerformanceItems(subList)
                hashMap[mapIndex] = Pair(periodAdapterData, viewPagerData)
                mapIndex++
            }
        }

        return hashMap
    }

    private fun getMonthlyDataSet(): HashMap<Int, Pair<String, List<PerformanceItem>>> {
        val hashMap = HashMap<Int, Pair<String, List<PerformanceItem>>>()
        val limitDate = LocalDateTime.now().minusMonths(6).withDayOfMonth(1)
        val travels = dataSet.sortedByDescending { it.initialDate }
            .filter { it.initialDate.isAfter(limitDate) }
            .groupBy { it.initialDate.month }
            .mapValues { (_, list) -> list.toMutableList() }
            .toList()

        var mapIndex = 0

        travels.forEachIndexed label@{ index, pair ->
            val nextInd = (index + 1)
            val isNextMonthInvalid =
                nextInd < travels.size && !travels[nextInd].second.first().considerAverage

            when {
                isNextMonthInvalid -> {
                    val nextMonthList = travels[nextInd].second
                    val thisMonthList = pair.second

                    val transferIndex = thisMonthList.indexOfLast { it.considerAverage }

                    thisMonthList.subList(transferIndex, thisMonthList.size).forEach {
                        nextMonthList.add(0, it)
                        thisMonthList.remove(it)
                    }

                    if(thisMonthList.size == 0) return@label
                }

                else -> {}
            }

            val periodAdapterData =
                "${pair.second.last().initialDate.getMonthInPtBrAbbreviated()} null null"
            val viewPagerData = getPerformanceItems(pair.second)
            hashMap[mapIndex] = Pair(periodAdapterData, viewPagerData)
            mapIndex++

        }

        return hashMap
    }

    private fun getYearlyDataSet(): HashMap<Int, Pair<String, List<PerformanceItem>>> {
        val hashMap = HashMap<Int, Pair<String, List<PerformanceItem>>>()
        var mapIndex = 0

        dataSet
            .sortedByDescending { it.initialDate }
            .groupBy { it.initialDate.year }
            .forEach { (_, travelList) ->
                val periodAdapterData =
                    "${travelList.first().initialDate.year.toString().substring(2)} null null"
                val viewPagerData = getPerformanceItems(travelList)
                hashMap[mapIndex] = Pair(periodAdapterData, viewPagerData)
                mapIndex++
            }

        return hashMap
    }

    private fun getPerformanceItems(travelList: List<Travel>): List<PerformanceItem> {
        val averageHit = useCase.getRefuelAverage(travelList)
        val averagePercent = averageHit.divide(averageAim, 2, RoundingMode.HALF_EVEN)
            .multiply(BigDecimal(100))

        val profitHit = useCase.getProfitPercentage(travelList)
        val profitPercent = profitHit.divide(performanceAim, 2, RoundingMode.HALF_EVEN)
            .multiply(BigDecimal(100))

        return listOf(
            PerformanceItem(
                title = "Média",
                meta = averageAim.toPlainString(),
                hit = averageHit.toString(),
                percent = "${averagePercent.toInt()}%",
                progressBar = averagePercent.toInt()
            ),
            PerformanceItem(
                title = "Desempenho",
                meta = "$performanceAim%",
                hit = "${profitHit.toInt()}%",
                percent = "${profitPercent.toInt()}%",
                progressBar = profitPercent.toInt()
            )
        )
    }


}
