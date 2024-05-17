package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.ExpendRepository
import br.com.apps.repository.repository.FreightRepository
import br.com.apps.repository.repository.RefuelRepository
import br.com.apps.repository.repository.TravelRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalCoroutinesApi::class)
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
     * Retrieves a complete list of travels associated with a driver ID.
     * The function merges data from different repositories, such as travel, freight, refuel, and expend lists.
     *
     * @param driverId The ID of the driver for whom to retrieve the travel list.
     * @return A LiveData object that emits responses containing the complete travel list.
     */
    suspend fun getCompleteTravelListByDriverId(driverId: String): LiveData<Response<List<Travel>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Travel>>>()
            var isFirstBoot = true

            CoroutineScope(Dispatchers.Main).launch {
                val deferredA = CompletableDeferred<List<Travel>>()
                val liveDataA = repository.getTravelListByDriverId(driverId,true)
                mediator.addSource(liveDataA) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let {
                            if(isFirstBoot) deferredA.complete(it)

                        }
                    }
                }

                deferredA.await()
                val travelList = deferredA.getCompleted()
                val idList = travelList.mapNotNull { travel -> travel.id }

                val deferredB = CompletableDeferred<List<Freight>>()
                val liveDataB = freightRepository.getFreightListByTravelId(idList, withFlow = true)
                mediator.addSource(liveDataB) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let {
                            if(isFirstBoot) deferredB.complete(it)
                            else mergeTravelData(travelList, freightList = it)
                        }
                    }
                }

                val deferredC = CompletableDeferred<List<Refuel>>()
                val liveDataC = refuelRepository.getRefuelListByTravelIds(idList, true)
                mediator.addSource(liveDataC) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let {
                            if(isFirstBoot) deferredC.complete(it)
                            else mergeTravelData(travelList, refuelList = it)
                        }
                    }
                }

                val deferredD = CompletableDeferred<List<Expend>>()
                val liveDataD = expendRepository.getExpendListByTravelId(idList, withFlow = true)
                mediator.addSource(liveDataD) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let {
                            if(isFirstBoot) deferredD.complete(it)
                            else mergeTravelData(travelList, expendList = it)
                        }
                    }
                }

                awaitAll(deferredB, deferredC, deferredD)
                val freightList = deferredB.getCompleted()
                val refuelList = deferredC.getCompleted()
                val expendList = deferredD.getCompleted()

                mergeTravelData(travelList, freightList, expendList, refuelList)
                isFirstBoot = false
                mediator.value = Response.Success(data = travelList)
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
            freightRepository.deleteFreightForThisTravel(travelId, id)
        }
        idsData.refuelIds.forEach { id ->
            refuelRepository.deleteRefuelForThisTravel(travelId, id)
        }
        idsData.expendIds.forEach { id ->
            expendRepository.deleteExpendForThisTravel(travelId, id)
        }

        repository.delete(travelId)
    }

    /**
     * get by travelId
     */
    suspend fun getTravelById(travelId: String): LiveData<Response<Travel>> {
        repository.getTravelById(travelId)
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