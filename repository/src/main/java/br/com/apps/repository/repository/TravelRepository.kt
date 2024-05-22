package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Travel
<<<<<<<<< Temporary merge branch 1
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.Response
import br.com.apps.repository.onComplete
import br.com.apps.repository.toTravelList
import br.com.apps.repository.toTravelObject
=========
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTravelList
import br.com.apps.repository.util.toTravelObject
import com.google.firebase.firestore.CollectionReference
>>>>>>>>> Temporary merge branch 2
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TravelRepository(fireStore: FirebaseFirestore) {

    private val read = TraRead(fireStore.collection(FIRESTORE_COLLECTION_TRAVELS))
    private val write = TraWrite(fireStore.collection(FIRESTORE_COLLECTION_TRAVELS))

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun save(dto: TravelDto) = write.save(dto)

    suspend fun delete(id: String) = write.delete(id)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getTravelListByDriverId(id: String, flow: Boolean = false) =
        read.getTravelListByDriverId(id, flow)

    suspend fun getTravelById(id: String, flow: Boolean = false) =
        read.getTravelById(id, flow)

}

private class TraRead(private val collection: CollectionReference) {

    /**
     * Fetches the [Travel] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel] list.
     */
    suspend fun getTravelListByDriverId(
        driverId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Travel>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

<<<<<<<<< Temporary merge branch 1
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
=========
        return if (flow) listener.onSnapShot { it.toTravelList() }
        else listener.onComplete { it.toTravelList() }
>>>>>>>>> Temporary merge branch 2
    }

    /**
     * Fetches the [Travel] dataSet for the specified ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel].
     */
    suspend fun getTravelById(travelId: String, flow: Boolean = false): LiveData<Response<Travel>> {
        val listener = collection.document(travelId)

        return if (flow) listener.onSnapShot { it.toTravelObject() }
        else listener.onComplete { it.toTravelObject() }
    }

}

private class TraWrite(private val collection: CollectionReference) {

    suspend fun delete(travelId: String) {
        collection
            .document(travelId)
            .delete()
            .await()
    }

    suspend fun save(dto: TravelDto) {
        if (dto.id == null) {
            create(dto)
        }
    }

    private suspend fun create(dto: TravelDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

}
