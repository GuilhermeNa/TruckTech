package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.Response
import br.com.apps.repository.toTravelList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TravelRepository(
    fireStore: FirebaseFirestore,
    private val freightRepository: FreightRepository,
    private val refuelRepository: RefuelRepository,
    private val expendRepository: ExpendRepository
) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)
    private val teste= fireStore.collectionGroup("teste")
    suspend fun getCompleteTravelsListByDriverId(driverId: String): LiveData<Response<List<Travel>>> {
        return coroutineScope {
            val liveData = MutableLiveData<Response<List<Travel>>>()
            val dataSet = mutableListOf<Travel>()

            collection.whereEqualTo(DRIVER_ID, driverId)
                .addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                        return@addSnapshotListener
                    }
                    querySnap?.let { query ->
                        val travelsList = query.toTravelList()
                        dataSet.clear()
                        dataSet.addAll(travelsList)

                        val deferredA = CompletableDeferred<Unit>()
                        val deferredB = CompletableDeferred<Unit>()
                        val deferredC = CompletableDeferred<Unit>()

                        freightRepository.getFreightListForThisTravelQuery(query, dataSet, deferredA)
                        refuelRepository.getRefuelListForThisTravelQuery(query, dataSet, deferredB)
                        expendRepository.getExpendListForThisTravelQuery(query, dataSet, deferredC)

                        CoroutineScope(Dispatchers.Main).launch {
                            awaitAll(deferredA, deferredB, deferredC)
                            liveData.postValue(Response.Success(data = dataSet))
                        }
                    }
                }

            return@coroutineScope liveData
        }
    }

    /**
     * Delete travel
     */
    suspend fun deleteTravel(travelId: String) {
        collection
            .document(travelId)
            .delete()
            .await()
    }

}

//---------------------------------------------------------------------------------------------//
// HELPERS
//---------------------------------------------------------------------------------------------//