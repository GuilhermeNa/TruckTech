package br.com.apps.trucktech.ui.fragments.nav_home.home.frag_performance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.model.model.travel.Travel
import br.com.apps.trucktech.expressions.getDayFormatted
import br.com.apps.trucktech.expressions.getKeyByValue
import br.com.apps.trucktech.expressions.getMonthInPtBrAbbreviated
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.TravelUseCase
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.InvalidParameterException
import java.time.LocalDateTime

private const val TRAVEL = "Viagem"
private const val MONTH = "Mês"
private const val YEAR = "Ano"

class PerformanceViewModel(private val useCase: TravelUseCase) : ViewModel() {

    private lateinit var dataSet: List<Travel>

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

    fun initialize(dataSet: List<Travel>) {

        this@PerformanceViewModel.dataSet = dataSet

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
     * Filters the data based on the selected header position.
     * Returns a HashMap containing the filtered data grouped by header position.
     *
     * @return A HashMap where the key is the header position, and the value is a Pair consisting of the header name
     *         and the corresponding list of performance items.
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

        dataSet.sortedByDescending { it.initialDate }.forEachIndexed { index, travel ->
            val periodAdapterData =
                travel.initialDate?.getDailyTitle() ?: throw InvalidParameterException()
            val viewPagerData = getPerformanceItems(listOf(travel))

            hashMap[index] = Pair(periodAdapterData, viewPagerData)
        }

        return hashMap
    }

    private fun getMonthlyDataSet(): HashMap<Int, Pair<String, List<PerformanceItem>>> {
        val hashMap = HashMap<Int, Pair<String, List<PerformanceItem>>>()
        var index = 0

        dataSet.sortedByDescending { it.initialDate }
            .groupBy { it.initialDate?.month ?: throw InvalidParameterException() }
            .forEach { (_, travelList) ->
                val recyclerData = travelList.first().initialDate?.getMonthInPtBrAbbreviated()
                    ?: throw InvalidParameterException()
                val viewPagerData = getPerformanceItems(travelList)
                hashMap[index] = Pair(recyclerData, viewPagerData)
                index++
            }

        return hashMap
    }

    private fun getYearlyDataSet(): HashMap<Int, Pair<String, List<PerformanceItem>>> {
        val hashMap = HashMap<Int, Pair<String, List<PerformanceItem>>>()
        var index = 0

        dataSet
            .sortedByDescending { it.initialDate }
            .groupBy { it.initialDate?.year ?: throw InvalidParameterException() }
            .forEach { (_, travelList) ->
                val recyclerData = travelList.first().initialDate?.year.toString().substring(2)
                val viewPagerData = getPerformanceItems(travelList)
                hashMap[index] = Pair(recyclerData, viewPagerData)
                index++
            }

        return hashMap
    }

    private fun LocalDateTime.getDailyTitle(): String {
        val day = this.getDayFormatted()
        val month = this.getMonthInPtBrAbbreviated()
        return "$month $day"
    }

    private fun getPerformanceItems(travelList: List<Travel>): List<PerformanceItem> {
        val averageGoal = BigDecimal(2.50)
        val averageHit = useCase.getRefuelAverage(travelList)
        val averagePercent =
            averageHit.divide(averageGoal, 2, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal(100))

        val profitGoal = BigDecimal(50)
        val profitHit = useCase.getProfitPercentage(travelList)
        val profitPercent =
            profitHit.divide(profitGoal, 2, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal(100))

        return listOf(
            PerformanceItem(
                title = "Média",
                meta = "2.50",
                hit = averageHit.toString(),
                percent = "${averagePercent.toInt()}%",
                progressBar = averagePercent.toInt()
            ),
            PerformanceItem(
                title = "Desempenho",
                meta = "${profitGoal.toPlainString()}%",
                hit = "${profitHit.toInt()}%",
                percent = "${profitPercent.toInt()}%",
                progressBar = profitPercent.toInt()
            )
        )
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

}

data class PerformanceItem(
    val title: String,
    val meta: String,
    val hit: String,
    val percent: String,
    var progressBar: Int
)