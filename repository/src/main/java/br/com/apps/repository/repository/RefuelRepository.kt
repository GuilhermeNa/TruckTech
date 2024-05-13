package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.FIRESTORE_COLLECTION_REFUELS
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.Response
import br.com.apps.repository.TRAVEL_ID
import br.com.apps.repository.toRefuelList
import br.com.apps.repository.toRefuelObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class RefuelRepository(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REFUELS)
    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    /**
     * Deletes a specific [Refuel] entry associated with a [Travel].
     *
     * @param travelId The ID of the travel from which to delete the refuel entry.
     * @param refuelId The ID of the refuel entry to delete.
     */
    suspend fun deleteRefuelForThisTravel(travelId: String, refuelId: String) {
        parentCollection
            .document(travelId)
            .collection(FIRESTORE_COLLECTION_REFUELS)
            .document(refuelId)
            .delete()
            .await()
    }

    /**
     * Fetches the [Refuel] dataSet based on the driver ID.
     *
     * @param driverId The ID of the driver for which [Refuel] data is to be retrieved.
     * @return A LiveData object containing a [Response] of [Refuel] data for the specified driver ID.
     */
    suspend fun getRefuelListByDriverId(driverId: String, withFlow: Boolean): LiveData<Response<List<Refuel>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Refuel>>>()
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)

            if(withFlow) {
                listener.addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        liveData.postValue(Response.Success(query.toRefuelList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toRefuelList()))
                    }
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Fetch the dataSet for the specified [Travel] ID.
     *
     * This function fetches expenditure data for the given travel ID asynchronously from the database.
     *
     * @param travelId The ID of the travel for which [Refuel] data is to be retrieved.
     * @return A LiveData object containing a [Response] of [Refuel] data for the specified travel ID.
     */
    suspend fun getRefuelListByTravelId(travelId: String, withFlow: Boolean): LiveData<Response<List<Refuel>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Refuel>>>()
            val listener = collection.whereEqualTo(TRAVEL_ID, travelId)

            if (withFlow) {
                listener.addSnapshotListener { snapQuery, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    snapQuery?.let { query ->
                        liveData.postValue(Response.Success(query.toRefuelList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toRefuelList()))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }

    /**
     * Fetch the dataSet for the specified [Travel] ID list.
     *
     * This function fetches expenditure data for the given travel ID asynchronously from the database.
     *
     * @param idList The ID list of the travel for which [Refuel] data is to be retrieved.
     * @return A LiveData object containing a [Response] of [Refuel] data for the specified travel ID.
     */
    suspend fun getRefuelListByTravelId(idList: List<String>, withFlow: Boolean): LiveData<Response<List<Refuel>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Refuel>>>()
            val listener = collection.whereIn(TRAVEL_ID, idList)

            if (withFlow) {
                listener.addSnapshotListener { snapQuery, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    snapQuery?.let { query ->
                        liveData.postValue(Response.Success(query.toRefuelList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toRefuelList()))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }


    /**
     * Fetch the data for a specific [Refuel] ID.
     *
     * @param refuelId The ID of the [Refuel] for which data is to be retrieved.
     * @param withFlow If the user wants to keep observing the source or not.
     * @return A LiveData object containing a [Response] of an [Refuel] data for the specified ID.
     */
    suspend fun getRefuelById(refuelId: String, withFlow: Boolean): LiveData<Response<Refuel>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<Refuel>>()

            if(withFlow) {
                collection
                    .document(refuelId)
                    .addSnapshotListener { documentSnap, error ->
                        error?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        documentSnap?.let { document ->
                            val refuel = document.toRefuelObject()
                            liveData.postValue(Response.Success(data = refuel))
                        }
                    }
            } else {
                collection
                    .document(refuelId)
                    .get()
                    .addOnCompleteListener { task ->
                        task.exception?.let { e->
                            liveData.postValue(Response.Error(e))
                        }
                        task.result?.let { document ->
                            val refuel = document.toRefuelObject()
                            liveData.postValue(Response.Success(refuel))
                        }
                    }
            }

            return@withContext liveData
        }
    }

    /**
     * Deletes an [Refuel] document from the database based on the specified ID.
     *
     * @param refuelId The ID of the document to be deleted.
     */
    suspend fun delete(refuelId: String) {
        collection
            .document(refuelId)
            .delete()
            .await()
    }

    /**
     * Saves the [RefuelDto] object.
     * If the ID of the [RefuelDto] is null, it creates a new [Refuel].
     * If the ID is not null, it updates the existing [Refuel].
     *
     * @param dto The [RefuelDto] object to be saved.
     */
    suspend fun save(dto: RefuelDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: RefuelDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: RefuelDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()

    }


}