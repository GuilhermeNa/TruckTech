package br.com.apps.usecase

import androidx.lifecycle.LiveData
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.EMPTY_DATASET
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.Response
import br.com.apps.repository.repository.FreightRepository
import java.security.InvalidParameterException

class FreightUseCase(
    private val repository: FreightRepository,
    private val labelUseCase: LabelUseCase
) {

    /**
     * Merge
     */
    fun mergeFreightListToTravelList(
        travelList: List<Travel>,
        freightListNullable: List<Freight>?
    ) {
        val freightList = freightListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val freights = freightList.filter { it.travelId == travelId }
            travel.freightsList = freights
        }
    }

    /**
     * get List
     */
    suspend fun getFreightListForThisTravelId(travelIds: List<String>): LiveData<Response<List<Freight>>> {
        return repository.getFreightListForThisTravel(travelIds)
    }

    /**
     * Delete
     */
    suspend fun deleteFreightForThisTravel(travelId: String, id: String) {
        repository.deleteFreightForThisTravel(travelId, id)
    }

/*    *//**
     * Get  freight by id
     *//*
    suspend fun getFreightById(freightId: String, getLabel: Boolean): LiveData<Response<Freight>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<Freight>>()

            if (!getLabel) {
                return@coroutineScope repository.getByFreightId(freightId, true)
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    lateinit var freight: Freight

                    val deferredA = CompletableDeferred<Unit>()
                    val liveDataA = repository.getByFreightId(freightId, true)
                    mediator.addSource(liveDataA) { responseA ->
                        when (responseA) {
                            is Response.Error -> mediator.value =
                                Response.Error(responseA.exception)

                            is Response.Success -> {
                                freight = responseA.data!!
                            }
                        }
                        deferredA.complete(Unit)
                    }

                    deferredA.await()

                    val liveDataB = labelUseCase.getLabelById(freight.labelId!!)
                    mediator.addSource(liveDataB) { responseB ->
                        when (responseB) {
                            is Response.Error -> mediator.value =
                                Response.Error(responseB.exception)

                            is Response.Success -> {
                                val label = responseB.data
                                freight.label = label
                                mediator.value = Response.Success(data = freight)
                            }
                        }
                    }
                }
            }

            return@coroutineScope mediator
        }
    }*/

    suspend fun delete(freightId: String) {
        repository.delete(freightId)
    }

}
