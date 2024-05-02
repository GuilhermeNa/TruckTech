package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.FIRESTORE_COLLECTION_FREIGHTS
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.Response
import br.com.apps.repository.TRAVEL_ID
import br.com.apps.repository.toFreightList
import br.com.apps.repository.toFreightObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

private const val IS_COMMISSION_PAID = "isCommissionPaid"

class FreightRepository(fireStore: FirebaseFirestore) {

    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)
    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    suspend fun getFreightListForThisTravel(idList: List<String>): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Freight>>>()
            val dataSet = mutableListOf<Freight>()
            var tasksCounter = 0

            idList.forEach { id ->
                collection.whereEqualTo(TRAVEL_ID, id)
                    .addSnapshotListener { snapQuery, error ->
                        error?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        snapQuery?.let { query ->
                            dataSet.addAll(query.toFreightList())
                            tasksCounter++
                            if (tasksCounter == idList.size)
                                liveData.postValue(Response.Success(data = dataSet))
                        }
                    }
            }

            return@withContext liveData
        }
    }

    suspend fun deleteFreightForThisTravel(travelId: String, freightId: String) {
        parentCollection
            .document(travelId)
            .collection(FIRESTORE_COLLECTION_FREIGHTS)
            .document(freightId)
            .delete()
            .await()
    }

    /**
     * Fetches the [Freight] dataSet based on the driver ID.
     *
     * @param driverId The ID of the driver for whom the [Freight] dataSet is to be retrieved
     * @param withFlow If the user wants to keep observing the source or not.
     * @return LiveData containing a [Response] object with either a list of [Freight] items or an error.
     */
    suspend fun getFreightListByDriverId(driverId: String, withFlow: Boolean): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Freight>>>()
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)

            if(withFlow) {
                listener.addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        liveData.postValue(Response.Success(query.toFreightList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toFreightList()))
                    }
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Retrieves a LiveData containing a list of [Freight] items based on a list of driver IDs and payment status.
     *
     * @param driverIdList A list of driver IDs.
     * @param isPaid A boolean indicating the payment status.
     * @return LiveData containing a [Response] object with either a list of [Freight] items or an error.
     */
    suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Freight>>>()
            val listener = collection
                .whereIn(DRIVER_ID, driverIdList)
                .whereEqualTo(IS_COMMISSION_PAID, isPaid)

            listener.get().addOnCompleteListener { task ->
                task.exception?.let { e ->
                    liveData.postValue(Response.Error(e))
                }
                task.result?.let { query ->
                    liveData.postValue(Response.Success(query.toFreightList()))
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Fetches the list of [Freight] dataSet based on the driver's ID and payment status.
     *
     * @param driverId The ID of the driver for whom the [Freight] dataSet is to be retrieved.
     * @param isPaid A boolean indicating whether the [Freight] is paid (true) or unpaid (false).
     * @param withFlow If the user wants to keep observing the source or not.
     * @return A LiveData object containing a [Response] of [Freight] dataSet.
     */
    suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        withFlow: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Freight>>>()
            val listener = collection
                .whereEqualTo(DRIVER_ID, driverId)
                .whereEqualTo(IS_COMMISSION_PAID, isPaid)

            if (withFlow) {
                listener.addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        liveData.postValue(Response.Success(query.toFreightList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toFreightList()))
                    }
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Fetches the [Freight] dataSet for the specified [Travel] ID.
     *
     * @param travelId The ID of the travel for which [Freight] dataSet is to be retrieved.
     * @return A LiveData object containing a [Response] of [Freight] dataSet.
     */
    suspend fun getFreightListByTravelId(
        travelId: String,
        withFlow: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Freight>>>()
            val listener = collection.whereEqualTo(TRAVEL_ID, travelId)

            if (withFlow) {
                listener.addSnapshotListener { snapQuery, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    snapQuery?.let { query ->
                        liveData.postValue(Response.Success(query.toFreightList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toFreightList()))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }

    /**
     * Fetches the [Freight] dataSet for the specified ID.
     *
     * @param freightId The ID of the [Freight] for which data is to be retrieved.
     * @param withFlow If the user wants to keep observing the source or not.
     * @return A LiveData object containing a [Response] of an [Freight] dataSet.
     */
    suspend fun getFreightById(freightId: String, withFlow: Boolean): LiveData<Response<Freight>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<Freight>>()
            val listener = collection.document(freightId)

            if (withFlow) {
                listener.addSnapshotListener { documentSnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    documentSnap?.let { document ->
                        val freight = document.toFreightObject()
                        liveData.postValue(Response.Success(data = freight))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { document ->
                        val freight = document.toFreightObject()
                        liveData.postValue(Response.Success(freight))
                    }
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Deletes an [Freight] document from the database based on the specified ID.
     *
     * @param freightId The ID of the document to be deleted.
     */
    suspend fun delete(freightId: String) {
        collection
            .document(freightId)
            .delete()
            .await()
    }

    /**
     * Saves the [FreightDto] object.
     * - If the ID of the [FreightDto] is null, it creates a new [Freight].
     * - If the ID is not null, it updates the existing [Freight].
     *
     * @param dto The [FreightDto] object to be saved.
     */
    suspend fun save(dto: FreightDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: FreightDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: FreightDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()
    }



}