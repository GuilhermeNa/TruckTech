package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.FineDto
import br.com.apps.model.model.Fine
import br.com.apps.model.model.Truck
import br.com.apps.model.model.employee.DriverEmployee
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.Response
import br.com.apps.repository.TRUCK_ID
import br.com.apps.repository.toFineList
import br.com.apps.repository.toFineObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

private const val FIRESTORE_COLLECTION_FINES = "fines"

class FineRepository(private val fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FINES)

    /**
     * Fetch the dataSet for the specified [DriverEmployee] ID.
     *
     * This function fetches [Fine] data for the given [DriverEmployee] ID asynchronously from the database.
     *
     * @param driverId The ID of the [DriverEmployee] for which [Fine] data is to be retrieved.
     * @return A LiveData object containing a [Response] of [Fine] data for the specified ID.
     */
    suspend fun getFineListByDriverId(driverId: String, withFlow: Boolean): LiveData<Response<List<Fine>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Fine>>>()
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)

            if (withFlow) {
                listener.addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        liveData.postValue(Response.Success(query.toFineList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let {  query ->
                        liveData.postValue(Response.Success(query.toFineList()))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }

    /**
     * Fetch the dataSet for the specified [Truck] ID.
     *
     * This function fetches [Fine] data for the given [Truck] ID asynchronously from the database.
     *
     * @param truckId The ID of the [Truck] for which [Fine] data is to be retrieved.
     * @return A LiveData object containing a [Response] of [Fine] data for the specified ID.
     */
    suspend fun getFineListByTruckId(truckId: String, withFlow: Boolean): LiveData<Response<List<Fine>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Fine>>>()
            val listener = collection.whereEqualTo(TRUCK_ID, truckId)

            if (withFlow) {
                listener.addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        liveData.postValue(Response.Success(query.toFineList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let {  query ->
                        liveData.postValue(Response.Success(query.toFineList()))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }

    /**
     * Fetch the data for a specific [Fine] ID.
     *
     * @param fineId The ID of the [Fine] for which data is to be retrieved.
     * @param withFlow If the user wants to keep observing the source or not.
     * @return A LiveData object containing a [Response] of an [Fine] data for the specified ID.
     */
    suspend fun getFineById(fineId: String, withFlow: Boolean): LiveData<Response<Fine>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<Fine>>()
            val listener = collection.document(fineId)

            if (withFlow) {
                listener.addSnapshotListener { documentSnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    documentSnap?.let { document ->
                        liveData.postValue(Response.Success(document.toFineObject()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { document ->
                        liveData.postValue(Response.Success(document.toFineObject()))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }

    /**
     * Deletes an [Fine] document from the database based on the specified ID.
     *
     * @param fineId The ID of the document to be deleted.
     */
    suspend fun delete(fineId: String) {
        collection
            .document(fineId)
            .delete()
            .await()
    }

    /**
     * Saves the [Fine] object.
     * If the ID of the [FineDto] is null, it creates a new [Fine].
     * If the ID is not null, it updates the existing [Fine].
     *
     * @param dto The [FineDto] object to be saved.
     */
    suspend fun save(dto: FineDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: FineDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: FineDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()

    }

}