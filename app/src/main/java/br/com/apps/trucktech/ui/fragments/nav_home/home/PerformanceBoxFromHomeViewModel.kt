package br.com.apps.trucktech.ui.fragments.nav_home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.ExpendRepository
import br.com.apps.repository.repository.FreightRepository
import br.com.apps.repository.repository.RefuelRepository
import br.com.apps.repository.repository.TravelRepository
import br.com.apps.trucktech.expressions.getDayFormatted
import br.com.apps.trucktech.expressions.getKeyByValue
import br.com.apps.trucktech.expressions.getMonthInPtBrAbbreviated
import br.com.apps.usecase.TravelUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.InvalidParameterException
import java.time.LocalDateTime

private const val TRAVEL = "Viagem"
private const val MONTH = "Mês"
private const val YEAR = "Ano"

class PerformanceBoxFromHomeViewModel(
    private val useCase: TravelUseCase,
    private val travelRepository: TravelRepository,
    private val freightRepository: FreightRepository,
    private val expendRepository: ExpendRepository,
    private val refuelRepository: RefuelRepository
) : ViewModel() {

    /**
     * Data to be considered for the performance box.
     */
    private lateinit var dataSet: List<Travel>

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

    /**
     * LiveData holding the [Response] with a map of data to be displayed on screen.
     */
    private val _viewData =
        MutableLiveData<Response<HashMap<Int, Pair<String, List<PerformanceItem>>>>>()
    val viewData get() = _viewData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    /**
     * Loads data asynchronously and set [_viewData] response.
     *  1. Load [Travel] data
     *  2. Load [Freight] data
     *  3. Load [Expend] data
     *  4. Load [Refuel] data
     *  5. Merge all in [Travel] List
     *  6. Set [dataSet]
     *  7. Defines the initial [_viewData]
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadData(driverId: String) {
        viewModelScope.launch {

            val liveDataA = travelRepository.getTravelListByDriverId(driverId, false)
            val deferredA = CompletableDeferred<List<Travel>>()
            launch {
                liveDataA.asFlow().first { response ->
                    when (response) {
                        is Response.Error -> {
                            response.exception.printStackTrace()
                            deferredA.complete(emptyList())
                        }

                        is Response.Success -> {
                            deferredA.complete(response.data!!)
                        }
                    }
                }
            }

            val liveDataB = freightRepository.getFreightListByDriverId(driverId, false)
            val deferredB = CompletableDeferred<List<Freight>>()
            launch {
                liveDataB.asFlow().first { response ->
                    when (response) {
                        is Response.Error -> {
                            response.exception.printStackTrace()
                            deferredB.complete(emptyList())
                        }

                        is Response.Success -> {
                            deferredB.complete(response.data!!)
                        }
                    }
                }
            }

            val liveDataC = expendRepository.getExpendListByDriverId(driverId, false)
            val deferredC = CompletableDeferred<List<Expend>>()
            launch {
                liveDataC.asFlow().first { response ->
                    when (response) {
                        is Response.Error -> {
                            response.exception.printStackTrace()
                            deferredC.complete(emptyList())
                        }

                        is Response.Success -> {
                            deferredC.complete(response.data!!)
                        }
                    }
                }
            }

            val liveDataD = refuelRepository.getRefuelListByDriverId(driverId, false)
            val deferredD = CompletableDeferred<List<Refuel>>()
            launch {
                liveDataD.asFlow().first { response ->
                    when (response) {
                        is Response.Error -> {
                            response.exception.printStackTrace()
                            deferredD.complete(emptyList())
                        }

                        is Response.Success -> {
                            deferredD.complete(response.data!!)
                        }
                    }
                }
            }

            awaitAll(deferredA, deferredB, deferredC, deferredD)

            val travelData = deferredA.getCompleted()
            useCase.mergeTravelData(
                travelList = travelData,
                freightList = deferredB.getCompleted(),
                expendList = deferredC.getCompleted(),
                refuelList = deferredD.getCompleted()
            )

            dataSet = travelData
            val viewData = filterDataByHeader()
            _viewData.value = Response.Success(viewData)

        }
    }

    /**
     * Retrieves the data for the ViewPager.
     *
     * @return A list of PerformanceItem objects representing the data for the ViewPager.
     */
    fun getPagerData(): List<PerformanceItem> {
        val data =
            (viewData.value as Response.Success<HashMap<Int, Pair<String, List<PerformanceItem>>>>).data
        return data?.get(periodPos)?.second ?: emptyList()
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
            _viewData.value = Response.Success(data = filterDataByHeader())
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
            val recyclerData =
                travel.initialDate?.getDailyTitle() ?: throw InvalidParameterException()
            val viewPagerData = getPerformanceItems(listOf(travel))
            hashMap[index] = Pair(recyclerData, viewPagerData)
        }

        return hashMap
    }

    private fun getMonthlyDataSet(): HashMap<Int, Pair<String, List<PerformanceItem>>> {
        val hashMap = HashMap<Int, Pair<String, List<PerformanceItem>>>()
        var index = 0
        dataSet
            .sortedByDescending { it.initialDate }
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

    private fun getYearlyDataSet(): java.util.HashMap<Int, Pair<String, List<PerformanceItem>>> {
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

}

data class PerformanceItem(
    val title: String,
    val meta: String,
    val hit: String,
    val percent: String,
    var progressBar: Int
)
