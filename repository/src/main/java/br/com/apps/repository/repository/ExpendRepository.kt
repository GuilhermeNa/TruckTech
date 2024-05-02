package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.FIRESTORE_COLLECTION_EXPENDS
import br.com.apps.repository.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.Response
import br.com.apps.repository.TRAVEL_ID
import br.com.apps.repository.toExpendList
import br.com.apps.repository.toExpendObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

private const val PAID_BY_EMPLOYEE = "paidByEmployee"
private const val ALREADY_REFUNDED = "alreadyRefunded"

class ExpendRepository(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)
    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    suspend fun getExpendListForThisTravelListId(idList: List<String>): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Expend>>>()
            val dataSet = mutableListOf<Expend>()
            var tasksCounter = 0

            idList.forEach { id ->
                collection.whereEqualTo(TRAVEL_ID, id)
                    .addSnapshotListener { snapQuery, error ->
                        error?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        snapQuery?.let { query ->
                            val expendList = query.toExpendList()
                            expendList.forEach { expend ->
                                val existingIndex = dataSet.indexOfFirst { it.id == expend.id }
                                if (existingIndex != -1) {
                                    dataSet[existingIndex] = expend
                                } else {
                                    dataSet.add(expend)
                                }
                            }
                            tasksCounter++
                            if (tasksCounter == idList.size) {
                                liveData.postValue(Response.Success(data = dataSet))
                            }
                        }
                    }
            }

            return@withContext liveData
        }
    }

    suspend fun deleteExpendForThisTravel(travelId: String, expendId: String) {
        parentCollection
            .document(travelId)
            .collection(FIRESTORE_COLLECTION_EXPENDS)
            .document(expendId)
            .delete()
            .await()
    }

    /**
     * Fetches [Expend] dataSet for the given driver ID asynchronously from the database.
     *
     * @param driverId The ID of the driver for whom the [Expend] dataSet is to be retrieved
     * @param withFlow If the user wants to keep observing the source or not.
     * @return A LiveData object containing a response of [Expend] data for the specified driver ID.
     */
    suspend fun getExpendListByDriverId(
        driverId: String,
        withFlow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Expend>>>()
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)

            if (withFlow) {
                listener.addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        liveData.postValue(Response.Success(query.toExpendList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toExpendList()))
                    }
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Retrieves a LiveData containing a list of [Expend] items based on a list of driver IDs and payment status.
     *
     * @param driverIdList A list of driver IDs.
     * @param paidByEmployee A boolean indicating whether the [Expend] was paid by the employee (true) or not (false).
     * @param alreadyRefunded A boolean indicating whether the [Expend] has already been refunded (true) or not (false).
     * @return LiveData containing a [Response] object with either a list of [Expend] items or an error.
     */
    suspend fun getExpendListByDriverIdAndRefundableStatus(
        driverIdList: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Expend>>>()
            val listener =
                collection
                    .whereIn(DRIVER_ID, driverIdList)
                    .whereEqualTo(PAID_BY_EMPLOYEE, paidByEmployee)
                    .whereEqualTo(ALREADY_REFUNDED, alreadyRefunded)

            listener.get().addOnCompleteListener { task ->
                task.exception?.let { e ->
                    liveData.postValue(Response.Error(e))
                }
                task.result?.let { query ->
                    liveData.postValue(Response.Success(query.toExpendList()))
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Fetches the [Expend] dataSet based on the driver's ID and refundable status
     * considering whether the expenditure was paid by the employee and whether it has already been refunded.
     *
     * @param driverId The ID of the driver for whom the expenditures are to be retrieved.
     * @param paidByEmployee A boolean indicating whether the [Expend] was paid by the employee (true) or not (false).
     * @param alreadyRefunded A boolean indicating whether the [Expend] has already been refunded (true) or not (false).
     * @param withFlow If the user wants to keep observing the source or not.
     *
     * @return A LiveData object containing a [Response] of [Expend] data for the specified driver ID.
     */
    suspend fun getExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        withFlow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Expend>>>()
            val listener =
                collection
                    .whereEqualTo(DRIVER_ID, driverId)
                    .whereEqualTo(PAID_BY_EMPLOYEE, paidByEmployee)
                    .whereEqualTo(ALREADY_REFUNDED, alreadyRefunded)

            if (withFlow) {
                listener.addSnapshotListener { querySnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    querySnap?.let { query ->
                        liveData.postValue(Response.Success(query.toExpendList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toExpendList()))
                    }
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Fetch the dataSet for the specified [Expend] ID.
     *
     * Fetches [Expend] dataSet for the given [Travel] ID asynchronously from the database.
     *
     * @param travelId The ID of the [Travel] for which expenditure data is to be retrieved.
     * @param withFlow If the user wants to keep observing the source or not.
     * @return A LiveData object containing a response of [Expend] data for the specified [Travel] ID.
     */
    suspend fun getExpendListByTravelId(
        travelId: String,
        withFlow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Expend>>>()
            val listener = collection.whereEqualTo(TRAVEL_ID, travelId)

            if (withFlow) {
                listener.addSnapshotListener { snapQuery, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    snapQuery?.let { query ->
                        liveData.postValue(Response.Success(query.toExpendList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toExpendList()))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }

    /**
     * Fetch the data for the specified [Expend] ID.
     *
     * @param expendId The ID of the [Expend] for which data is to be retrieved.
     * @param withFlow If the user wants to keep observing the source or not.
     * @return A LiveData object containing a [Response] of an [Expend] data for the specified expenditure ID.
     */
    suspend fun getExpendById(expendId: String, withFlow: Boolean): LiveData<Response<Expend>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<Expend>>()
            val listener = collection.document(expendId)

            if (withFlow) {
                listener.addSnapshotListener { documentSnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    documentSnap?.let { document ->
                        val expend = document.toExpendObject()
                        liveData.postValue(Response.Success(data = expend))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { document ->
                        val expend = document.toExpendObject()
                        liveData.postValue(Response.Success(data = expend))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }

    /**
     * Deletes an [Expend] document from the database based on the specified ID.
     *
     * @param expendId The ID of the expenditure document to be deleted.
     */
    suspend fun delete(expendId: String) {
        collection
            .document(expendId)
            .delete()
            .await()
    }

    /**
     * Saves the [ExpendDto] object.
     * If the ID of the [ExpendDto] is null, it creates a new [Expend].
     * If the ID is not null, it updates the existing [Expend].
     *
     * @param dto The [ExpendDto] object to be saved.
     */
    suspend fun save(dto: ExpendDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: ExpendDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: ExpendDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()

    }

}