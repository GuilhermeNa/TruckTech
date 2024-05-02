package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.Response
import br.com.apps.repository.repository.TravelRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

class TravelUseCase(
    private val repository: TravelRepository,
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
        freightList?.let {
            freightUseCase.mergeFreightList(travelList, it)
        }
        expendList?.let {
            expendUseCase.mergeExpendList(travelList, it)
        }
        refuelList?.let {
            refuelUseCase.mergeRefuelList(travelList, it)
        }
    }

    /**
     * get by driverId
     */
    suspend fun getTravelListByDriverId(driverId: String): LiveData<Response<List<Travel>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Travel>>>()
            mediator.addSource(repository.getTravelListByDriverId(driverId)) { response ->
                when (response) {
                    is Response.Error -> mediator.postValue(response)
                    is Response.Success -> {
                        response.data?.let { travelList ->
                            val idList = travelList.mapNotNull { it.id }

                            CoroutineScope(Dispatchers.Main).launch {
                                val deferredA = CompletableDeferred<Unit>()
                                val deferredB = CompletableDeferred<Unit>()
                                val deferredC = CompletableDeferred<Unit>()

                                val liveDataA = freightUseCase.getFreightListForThisTravelId(idList)
                                val liveDataB = refuelUseCase.getRefuelForThisTravelId(idList)
                                val liveDataC = expendUseCase.getExpendListForThisTravel(idList)

                                mediator.addSource(liveDataA) { responseA ->
                                    when (responseA) {
                                        is Response.Error -> {
                                            mediator.value = Response.Error(responseA.exception)
                                        }

                                        is Response.Success -> {
                                            freightUseCase.mergeFreightList(
                                                travelList,
                                                responseA.data
                                            )
                                            deferredA.complete(Unit)
                                        }
                                    }
                                }
                                mediator.addSource(liveDataB) { responseB ->
                                    when (responseB) {
                                        is Response.Error ->
                                            mediator.value = Response.Error(responseB.exception)

                                        is Response.Success -> {
                                            refuelUseCase
                                                .mergeRefuelList(travelList, responseB.data)
                                            deferredB.complete(Unit)
                                        }
                                    }
                                }
                                mediator.addSource(liveDataC) { responseC ->
                                    when (responseC) {
                                        is Response.Error ->
                                            mediator.value = Response.Error(responseC.exception)

                                        is Response.Success -> {
                                            expendUseCase
                                                .mergeExpendList(travelList, responseC.data)
                                            deferredC.complete(Unit)
                                        }
                                    }
                                }

                                awaitAll(deferredA, deferredB, deferredC)
                                mediator.postValue(Response.Success(data = travelList))
                            }

                        }
                    }
                }
            }
            return@coroutineScope mediator
        }
    }

    /**
     * delete
     */
    suspend fun deleteTravel(idsData: TravelIdsData) {
        val travelId = idsData.travelId

        idsData.freightIds.forEach { id ->
            freightUseCase.deleteFreightForThisTravel(travelId, id)
        }
        idsData.refuelIds.forEach { id ->
            refuelUseCase.deleteRefuelForThisTravel(travelId, id)
        }
        idsData.expendIds.forEach { id ->
            expendUseCase.deleteExpendForThisTravel(travelId, id)
        }

        repository.deleteTravel(travelId)
    }

    /**
     * get by travelId
     */
    suspend fun getTravelById(travelId: String): LiveData<Response<Travel>> {
        return repository.getTravelById(travelId)
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
            wasteSum = wasteSum.add(travel.expendsList?.sumOf { it.value!! })
            wasteSum = wasteSum.add(travel.refuelsList?.sumOf { it.totalValue!! })
        }

        val percentage =
            if (profitSum != BigDecimal.ZERO) {
                wasteSum.divide(profitSum, 2, RoundingMode.HALF_EVEN).subtract(BigDecimal(1))
            }
            else BigDecimal.ZERO

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
            travelList.flatMap { it.refuelsList!! }.sumOf { it.totalValue!! }

        val sumOfKm =
            travelList.last().finalOdometerMeasurement
                ?.subtract(travelList.first().initialOdometerMeasurement)

        return sumOfKm?.divide(sumOfGasCost, 2, RoundingMode.HALF_EVEN) ?: BigDecimal.ZERO
    }

}

data class TravelIdsData(
    val travelId: String,
    val freightIds: List<String>,
    val refuelIds: List<String>,
    val expendIds: List<String>
) : Serializable