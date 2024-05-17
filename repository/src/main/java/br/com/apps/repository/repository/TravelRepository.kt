package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.Response
import br.com.apps.repository.onComplete
import br.com.apps.repository.toTravelList
import br.com.apps.repository.toTravelObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TravelRepository(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    /**
     * Fetches the [Travel] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the driver for whom the [Travel] dataSet is to be retrieved.
     * @param withFlow If the user wants to keep observing the source or not.
     * @return A LiveData object containing a [Response] of an [Travel] dataSet.
     */
    suspend fun getTravelListByDriverId(
        driverId: String,
        withFlow: Boolean
    ): LiveData<Response<List<Travel>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Travel>>>()
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)

            listener.onComplete {  }
            if (withFlow) {
                listener.addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        liveData.postValue(Response.Success(query.toTravelList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toTravelList()))
                    }
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

    suspend fun save(dto: TravelDto) {
        if(dto.id == null) {
            create(dto)
        }
    }

    suspend fun create(dto: TravelDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

}

//---------------------------------------------------------------------------------------------//
// HELPERS
//---------------------------------------------------------------------------------------------//