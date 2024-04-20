package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.Response
import br.com.apps.repository.toTravelList
import br.com.apps.repository.toTravelObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TravelRepository(
    fireStore: FirebaseFirestore,
    private val freightRepository: FreightRepository,
    private val refuelRepository: RefuelRepository,
    private val expendRepository: ExpendRepository
) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    suspend fun getTravelListByDriverId(driverId: String): LiveData<Response<List<Travel>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Travel>>>()

            collection.whereEqualTo(DRIVER_ID, driverId)
                .addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                        return@addSnapshotListener
                    }
                    querySnap?.let { query ->
                        liveData.postValue(
                            Response.Success(data = query.toTravelList())
                        )
                    }
                }

            return@withContext liveData
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

    /**
     * get travel by id
     */
    suspend fun getTravelById(travelId: String): LiveData<Response<Travel>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<Travel>>()

            collection
                .document(travelId)
                .addSnapshotListener { documentSnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    documentSnap?.let { document ->
                        val travel = document.toTravelObject()
                        liveData.postValue(Response.Success(travel))
                    }
                }

            return@withContext liveData
        }
    }

}

//---------------------------------------------------------------------------------------------//
// HELPERS
//---------------------------------------------------------------------------------------------//