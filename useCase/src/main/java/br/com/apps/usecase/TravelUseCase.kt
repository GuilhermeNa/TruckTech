package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

class TravelUseCase(
    private val repository: TravelRepository,
    private val freightUseCase: FreightUseCase,
    private val refuelUseCase: RefuelUseCase,
    private val expendUseCase: ExpendUseCase
) {

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
                                            freightUseCase.mergeFreightListToTravelList(travelList, responseA.data)
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

}

data class TravelIdsData(
    val travelId: String,
    val freightIds: List<String>,
    val refuelIds: List<String>,
    val expendIds: List<String>
) : Serializable