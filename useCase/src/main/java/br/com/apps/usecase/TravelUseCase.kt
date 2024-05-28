package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

class TravelUseCase(
    private val repository: TravelRepository,
    private val freightRepository: FreightRepository,
    private val refuelRepository: RefuelRepository,
    private val expendRepository: ExpendRepository,

    private val freightUseCase: FreightUseCase,
    private val refuelUseCase: RefuelUseCase,
    private val expendUseCase: ExpendUseCase
) {

    /**
     * This function allows merging lists of freights, expenditures, and refuels into the corresponding travels.
     *
     * @param travelList The list of travels into which additional data will be merged.
     * @param freightList The list of freights to merge into the travels. Defaults to null.
     * @param expendList The list of expenditures to merge into the travels. Defaults to null.
     * @param refuelList The list of refuels to merge into the travels. Defaults to null.
     */
    fun mergeTravelData(
        travelList: List<Travel>,
        freightList: List<Freight>? = null,
        expendList: List<Expend>? = null,
        refuelList: List<Refuel>? = null
    ) {
        freightList?.let { freightUseCase.mergeFreightList(travelList, it) }
        expendList?.let { expendUseCase.mergeExpendList(travelList, it) }
        refuelList?.let { refuelUseCase.mergeRefuelList(travelList, it) }
    }

    /**
     * delete
     */
    suspend fun deleteTravel(idsData: TravelIdsData) {
        val travelId = idsData.travelId

        withContext(Dispatchers.Main) {
            launch { idsData.freightIds.forEach { id -> freightRepository.delete(id) } }
            launch { idsData.refuelIds.forEach { id -> refuelRepository.delete(id) } }
            launch { idsData.expendIds.forEach { id -> expendRepository.delete(id) } }

            repository.delete(travelId)
        }

    }

    /**
     * Calculates the profit percentage based on the provided list of travels.
     * Profit percentage is calculated as the ratio of total waste (expenditures and refuels)
     * to total profit (freight earnings).
     *
     * @param travelList The list of travels for which the profit percentage needs to be calculated.
     * @return The profit percentage as a BigDecimal value.
     */
    fun getProfitPercentage(travelList: List<Travel>): BigDecimal {
        var profitSum = BigDecimal.ZERO
        var wasteSum = BigDecimal.ZERO

        travelList.map { travel ->
            profitSum = profitSum.add(travel.freightsList?.sumOf { it.value!! })
            wasteSum = wasteSum.add(travel.expendsList?.sumOf { it.value })
            wasteSum = wasteSum.add(travel.refuelsList?.sumOf { it.totalValue })
        }

        val percentage =
            if (profitSum != BigDecimal.ZERO) {
                wasteSum.divide(profitSum, 2, RoundingMode.HALF_EVEN).subtract(BigDecimal(1))
            } else BigDecimal.ZERO

        return percentage.abs().multiply(BigDecimal(100))
    }

    /**
     * Calculates the average refuel cost per kilometer traveled based on the provided list of travels.
     *
     * @param travelList The list of travels for which the refuel average needs to be calculated.
     * @return The average refuel cost per kilometer as a BigDecimal value.
     */
    fun getRefuelAverage(travelList: List<Travel>): BigDecimal {
        val sumOfGasCost =
            travelList.flatMap { it.refuelsList!! }.sumOf { it.totalValue }

        val sumOfKm =
            travelList.last().finalOdometerMeasurement
                ?.subtract(travelList.first().initialOdometerMeasurement)

        return sumOfKm?.divide(sumOfGasCost, 2, RoundingMode.HALF_EVEN) ?: BigDecimal.ZERO
    }

    /**
     * Retrieves a complete list of travels associated with a driver ID.
     * The function merges data from different repositories, such as travel, freight, refuel, and expend lists.
     *
     * @param driverId The ID of the driver for whom to retrieve the travel list.
     * @return A LiveData object that emits responses containing the complete travel list.
     */
    suspend fun getCompleteTravelListByDriverId(driverId: String): LiveData<Response<List<Travel>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Travel>>>()
            lateinit var freights: List<Freight>
            lateinit var expends: List<Expend>
            lateinit var refuels: List<Refuel>

            var isFirstBoot = true

            CoroutineScope(Dispatchers.Main).launch {
                val deferredA = CompletableDeferred<List<Travel>>()
                val liveDataA = repository.getTravelListByDriverId(driverId, true)
                mediator.addSource(liveDataA) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let {
                            if (isFirstBoot) deferredA.complete(it)
                            else {
                                mergeTravelData(it, freights, expends, refuels)
                                mediator.value = Response.Success(it)
                            }
                        }
                    }
                }

                val travels = deferredA.await()
                val travelIdList = travels.mapNotNull { t -> t.id }

                val deferredB = CompletableDeferred<List<Freight>>()
                val liveDataB = freightRepository.getFreightListByTravelIds(travelIdList, true)
                mediator.addSource(liveDataB) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let {
                            freights = it
                            if (isFirstBoot) deferredB.complete(it)
                            else {
                                mergeTravelData(travels, freightList = it)
                                mediator.value = Response.Success(travels)
                            }
                        }
                    }
                }

                val deferredC = CompletableDeferred<List<Refuel>>()
                val liveDataC = refuelRepository.getRefuelListByTravelIds(travelIdList, true)
                mediator.addSource(liveDataC) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let {
                            refuels = it
                            if (isFirstBoot) deferredC.complete(it)
                            else {
                                mergeTravelData(travels, refuelList = it)
                                mediator.value = Response.Success(travels)
                            }
                        }
                    }
                }

                val deferredD = CompletableDeferred<List<Expend>>()
                val liveDataD = expendRepository.getExpendListByTravelIds(travelIdList, true)
                mediator.addSource(liveDataD) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let {
                            expends = it
                            if (isFirstBoot) deferredD.complete(it)
                            else {
                                mergeTravelData(travels, expendList = it)
                                mediator.value = Response.Success(travels)
                            }
                        }
                    }
                }

                val freightList = deferredB.await()
                val refuelList = deferredC.await()
                val expendList = deferredD.await()

                mergeTravelData(travels, freightList, expendList, refuelList)
                isFirstBoot = false
                mediator.value = Response.Success(data = travels)
            }

            return@coroutineScope mediator
        }
    }

}

data class TravelIdsData(
    val travelId: String,
    val freightIds: List<String>,
    val refuelIds: List<String>,
    val expendIds: List<String>
) : Serializable