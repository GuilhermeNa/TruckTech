package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.repository.DRIVER_ID
import br.com.apps.repository.Resource
import br.com.apps.repository.UNKNOWN_EXCEPTION
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

private const val FIRESTORE_COLLECTION_REQUESTS = "requests"
private const val FIRESTORE_COLLECTION_ITEMS = "requestItems"

/**
 * Repository class responsible for interacting with Firestore to manage payment requests and items.
 * @param fireStore The instance of FirebaseFirestore to use for database operations.
 */
class RequestRepository(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    /**
     * Retrieves all payment requests and their associated items for a given driver ID from Firestore.
     *
     * @param driverId The ID of the driver for whom the requests and items should be retrieved.
     * @return A LiveData<Resource<List<PaymentRequest>>> representing the result of the retrieval operation:
     * - If the retrieval is successful, it emits a Resource with data set to a list of PaymentRequest objects.
     * - If an error occurs during retrieval, it emits a Resource with an empty list and an error message.
     */
    fun getAllRequestsAndItemsByDriverId(driverId: String): LiveData<Resource<List<PaymentRequest>>> {
        val liveData = MutableLiveData<Resource<List<PaymentRequest>>>()

        collection.whereEqualTo(DRIVER_ID, driverId)
            .addSnapshotListener { s, _ ->
                val requestList = mutableListOf<PaymentRequest>()
                try {
                    s?.let { query ->
                        query.documents.mapNotNull { document ->

                            val paymentRequest =
                                document.toObject<PaymentRequestDto>()?.toModel()

                            paymentRequest?.let {
                                getItemsListAndAddToRequest(document, it) {
                                    liveData.postValue(Resource(data = requestList))
                                }
                                requestList.add(it)
                            }

                        }
                    }
                } catch (e: Exception) {
                    val message = when (e) {
                        is NumberFormatException -> e.message
                        else -> UNKNOWN_EXCEPTION
                    }
                    e.printStackTrace()
                    liveData.value = Resource(data = emptyList(), error = message)
                }
            }

        return liveData
    }

    private fun getItemsListAndAddToRequest(
        document: DocumentSnapshot,
        paymentRequest: PaymentRequest,
        response: () -> Unit,
    ) {
        document.reference.collection(FIRESTORE_COLLECTION_ITEMS)
            .addSnapshotListener { s, _ ->
                s?.let { query ->
                    val itemsList = query.documents.mapNotNull { document ->
                        document.toObject<RequestItemDto>()?.toModel()
                    }
                    paymentRequest.itemsList?.clear()
                    paymentRequest.itemsList?.addAll(itemsList)
                    response()
                }
            }
    }

    /**
     * Retrieves the payment request and its associated items for the specified request ID from Firestore.
     *
     * @param requestId The ID of the request for which the request and items should be retrieved.
     * @return A LiveData<Resource<PaymentRequest>> representing the result of the retrieval operation:
     * - If the retrieval is successful, it emits a Resource with data set to a PaymentRequest object.
     * - If an error occurs during retrieval, it emits a Resource with a default PaymentRequest object and an error message.
     */
    fun getRequestAndItemsById(requestId: String): LiveData<Resource<PaymentRequest>> {
        val liveData = MutableLiveData<Resource<PaymentRequest>>()

        collection.document(requestId)
            .addSnapshotListener { d, _ ->
                try {
                    d?.let { document ->

                        val paymentRequest = document.toObject<PaymentRequestDto>()?.toModel()

                        paymentRequest?.let {
                            getItemsListAndAddToRequest(document, it) {
                                liveData.postValue(Resource(data = it))
                            }
                        }

                    }
                } catch (e: Exception) {
                    val message = when (e) {
                        is InvalidTypeException -> e.message
                        else -> UNKNOWN_EXCEPTION
                    }
                    e.printStackTrace()
                    liveData.postValue(Resource(data = PaymentRequest(), error = message))
                }
            }

        return liveData
    }

    /**
     * Deletes the item associated with the specified item ID from the request with the specified request ID in Firestore.
     *
     * @param requestId The ID of the request from which the item should be deleted.
     * @param itemId The ID of the item to be deleted.
     * @return A LiveData<Resource<Boolean>> representing the result of the deletion operation:
     * - If the deletion is successful, it emits a Resource with data set to true.
     * - If an error occurs during deletion, it emits a Resource with data set to false and an error message.
     */
    fun deleteItem(requestId: String, itemId: String): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()

        collection
            .document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS)
            .document(itemId)
            .delete()
            .addOnSuccessListener {
                liveData.postValue(Resource(data = true))
            }
            .addOnFailureListener { _ ->
                liveData.postValue(Resource(data = false, "Falha ao remover"))
            }

        return liveData

    }

    /**
     * Saves the request item data associated with the specified request ID to Firestore.
     *
     * If the [requestItemDto] contains an ID, it updates the existing document.
     *
     * If the [requestItemDto] does not contain an ID, it creates a new document and assigns an ID to the requestItemDto.
     *
     * @param requestId The ID of the request to which the item belongs.
     * @param requestItemDto The RequestItemDto object containing the data to be saved.
     * @return The ID of the saved document in Firestore.
     */
    suspend fun saveItem(requestId: String, requestItemDto: RequestItemDto): String {
        var document: DocumentReference?
        if (requestItemDto.id != null) {
            document = collection.document(requestId)
                .collection(FIRESTORE_COLLECTION_ITEMS)
                .document(requestItemDto.id!!)
                .get()
                .await()
                .reference
        } else {
            collection.document(requestId)
                .collection(FIRESTORE_COLLECTION_ITEMS)
                .document().also {
                    requestItemDto.id = it.id
                    document = it
                }
        }

        document?.set(requestItemDto)
        return document?.id ?: ""
    }

    /**
     * Saves the payment request data in Firestore.
     *
     * If the [requestDto] contains an ID, it updates the existing document.
     *
     * If the [requestDto] does not contain an ID, it creates a new document and assigns an ID to the requestDto.
     *
     * @param requestDto The PaymentRequestDto object containing the data to be saved.
     * @return The ID of the saved document in Firestore.
     */
    fun saveRequest(requestDto: PaymentRequestDto): String {
        val document =
            if (requestDto.id != null) {
                collection.document(requestDto.id!!)
            } else {
                collection.document().also {
                    requestDto.id = it.id
                }
            }

        document.set(requestDto)
        return document.id
    }

    /**
     * This method is responsible for deleting the requests in Firestore.
     * @param requestId Is the ID of the request that must be deleted.
     * @return A LiveData resource with a boolean indicating if the operation was successful.
     */
    fun deleteRequest(requestId: String): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        collection.document(requestId).delete()
            .addOnSuccessListener {
                liveData.postValue(Resource(data = true))
            }
        return liveData
    }

}

