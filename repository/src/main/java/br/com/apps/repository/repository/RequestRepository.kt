package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.REQUEST_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toRequestItemList
import br.com.apps.repository.util.toRequestItemObject
import br.com.apps.repository.util.toRequestList
import br.com.apps.repository.util.toRequestObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

private const val FIRESTORE_COLLECTION_REQUESTS = "requests"
private const val FIRESTORE_COLLECTION_ITEMS = "requestItems"

private const val ENCODED_IMAGE = "encodedImage"

/**
 * Repository class responsible for interacting with Firestore to manage payment requests and items.
 * @param fireStore The instance of FirebaseFirestore to use for database operations.
 */
class RequestRepository(private val fireStore: FirebaseFirestore) {

    private val read = ReqRead(fireStore)
    private val write = ReqWrite(fireStore)

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun save(dto: PaymentRequestDto) = write.save(dto)

    suspend fun saveItem(dto: RequestItemDto) = write.saveItem(dto)

    suspend fun delete(id: String, itemsId: List<String>? = null) = write.delete(id, itemsId)

    suspend fun deleteItem(requestId: String, itemId: String) = write.deleteItem(requestId, itemId)

    suspend fun updateEncodedImage(requestId: String, encodedImage: String) =
        write.updateEncodedImage(requestId, encodedImage)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getCompleteRequestListByDriverId(driverId: String, flow: Boolean = false) =
        read.getCompleteRequestListByDriverId(driverId, flow)

    suspend fun getCompleteRequestById(requestId: String, flow: Boolean = false) =
        read.getCompleteRequestById(requestId, flow)

    suspend fun getItemById(requestId: String, itemId: String, flow: Boolean = false) =
        read.getItemById(requestId, itemId, flow)

}

@OptIn(ExperimentalCoroutinesApi::class)
private class ReqRead(private val fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    /**
     * Fetches the [PaymentRequest] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    suspend fun getCompleteRequestListByDriverId(driverId: String, flow: Boolean)
            : LiveData<Response<List<PaymentRequest>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<PaymentRequest>>>()

            val deferred = CompletableDeferred<List<PaymentRequest>>()
            if (flow) {
                collection.whereEqualTo(DRIVER_ID, driverId)
                    .addSnapshotListener { querySnap, error ->
                        error?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        querySnap?.let { query ->
                            val requestList = query.toRequestList()
                            deferred.complete(requestList)
                        }
                    }
            } else {
                collection.whereEqualTo(DRIVER_ID, driverId).get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        val requestList = query.toRequestList()
                        deferred.complete(requestList)
                    }
                }
            }

            deferred.await()
            val requestList = deferred.getCompleted()
            val idList = requestList.mapNotNull { it.id }

            if (flow) {
                fireStore.collectionGroup(FIRESTORE_COLLECTION_ITEMS)
                    .whereIn(REQUEST_ID, idList)
                    .addSnapshotListener { querySnap, error ->
                        error?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        querySnap?.let { query ->
                            val itemList = query.toRequestItemList()
                            mergeRequestData(requestList, itemList)
                            liveData.postValue(Response.Success(requestList))
                        }
                    }
            } else {
                fireStore.collectionGroup(FIRESTORE_COLLECTION_ITEMS)
                    .whereIn(REQUEST_ID, idList)
                    .get()
                    .addOnCompleteListener { task ->
                        task.exception?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        task.result?.let { query ->
                            val itemList = query.toRequestItemList()
                            mergeRequestData(requestList, itemList)
                            liveData.postValue(Response.Success(requestList))
                        }
                    }
            }

            return@withContext liveData
        }
    }

    private fun mergeRequestData(requestList: List<PaymentRequest>, itemList: List<RequestItem>) {
        requestList.forEach { request ->
            val requestId = request.id ?: throw InvalidParameterException(EMPTY_ID)
            val items = itemList.filter { it.requestId == requestId }
            request.itemsList?.clear()
            request.itemsList?.addAll(items)
        }
    }

    /**
     * Fetches the [PaymentRequest] dataSet for the specified ID.
     *
     * @param requestId The ID of the [PaymentRequest].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    suspend fun getCompleteRequestById(
        requestId: String,
        flow: Boolean
    ): LiveData<Response<PaymentRequest>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<PaymentRequest>>()
            lateinit var request: PaymentRequest

            val deferredA = CompletableDeferred<PaymentRequest>()
            if (flow) {
                collection.document(requestId)
                    .addSnapshotListener { documentSnap, error ->
                        error?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        documentSnap?.let { document ->
                            document.toRequestObject()?.let {
                                deferredA.complete(it)
                            }
                        }
                    }
            } else {
                collection.document(requestId).get()
                    .addOnCompleteListener { task ->
                        task.exception?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        task.result?.let { document ->
                            document.toRequestObject()?.let { deferredA.complete(it) }
                        }
                    }
            }

            val deferredB = CompletableDeferred<List<RequestItem>>()
            var isFirstBoot = true
            if (flow) {
                collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS)
                    .addSnapshotListener { querySnap, error ->
                        error?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        querySnap?.let { query ->
                            val itemList = query.toRequestItemList()
                            if (isFirstBoot) {
                                deferredB.complete(itemList)
                                isFirstBoot = false
                            } else {
                                request.itemsList?.clear()
                                request.itemsList?.addAll(itemList)
                                liveData.postValue(Response.Success(request))
                            }
                        }
                    }
            } else {
                collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS)
                    .get().addOnCompleteListener { task ->
                        task.exception?.let { e ->
                            liveData.postValue(Response.Error(e))
                        }
                        task.result?.let { query ->
                            val itemList = query.toRequestItemList()
                            deferredB.complete(itemList)
                        }
                    }
            }

            awaitAll(deferredA, deferredB)
            request = deferredA.getCompleted()
            val itemList = deferredB.getCompleted()
            request.itemsList?.addAll(itemList)
            liveData.postValue(Response.Success(request))

            return@withContext liveData
        }
    }

    /**
     * Fetches the [PaymentRequest] dataSet for the specified ID.
     *
     * @param requestId The ID of the [PaymentRequest].
     * @param itemId The ID of the [RequestItem].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    suspend fun getItemById(
        requestId: String,
        itemId: String,
        flow: Boolean = false
    ): LiveData<Response<RequestItem>> {
        val listener = collection.document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS).document(itemId)

        return if(flow) listener.onSnapShot { it.toRequestItemObject() }
        else listener.onComplete { it.toRequestItemObject() }
    }

}

private class ReqWrite(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    /**
     * Saves the [RequestItemDto] data in Firestore.
     *
     *  - If the ID of the Item is null, it creates a new Item.
     *  - If the ID is not null, it updates the existing Item.
     *
     * @param itemDto The [RequestItemDto] object to be saved.
     */
    suspend fun saveItem(itemDto: RequestItemDto) {
        if (itemDto.id == null) {
            createItem(itemDto)
        } else {
            updateItem(itemDto)
        }
    }

    private suspend fun createItem(itemDto: RequestItemDto): String {
        val requestId = itemDto.requestId
            ?: throw NullPointerException("RequestRepository, createItem: requestId is null")

        val document =
            collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS).document()

        itemDto.id = document.id

        document
            .set(itemDto)
            .await()

        return document.id
    }

    private suspend fun updateItem(itemDto: RequestItemDto) {
        val requestId = itemDto.requestId
            ?: throw NullPointerException("RequestRepository, updateItem: requestId is null")

        val itemId = itemDto.id
            ?: throw NullPointerException("RequestRepository, updateItem: id is null")

        collection.document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS)
            .document(itemId)
            .set(itemDto)
            .await()

    }

    /**
     * Updates the encoded image for a specific document in the Firestore collection.
     *
     * @param requestId The ID of the document to which the encoded image will be updated.
     * @param encodedImage The encoded image in String to be updated in the document.
     */
    suspend fun updateEncodedImage(requestId: String, encodedImage: String) {
        collection
            .document(requestId)
            .update(ENCODED_IMAGE, encodedImage)
            .await()
    }

    /**
     * Saves the [PaymentRequestDto] data in Firestore.
     *
     *  - If the ID of the Request Dto is null, it creates a new Request.
     *  - If the ID is not null, it updates the existing Request.
     *
     * @param dto The [PaymentRequestDto] object to be saved.
     */
    suspend fun save(dto: PaymentRequestDto): String {
        return if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: PaymentRequestDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: PaymentRequestDto): String {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()

        return id
    }

    /**
     * This method is responsible for deleting the requests in Firestore.
     * @param requestId Is the ID of the request that must be deleted.
     */
    suspend fun delete(requestId: String, itemIdList: List<String>? = null) {
        itemIdList?.forEach { itemId ->
            deleteItem(requestId, itemId)
        }

        collection
            .document(requestId)
            .delete()
            .await()

    }

    /**
     * Deletes the item associated with the specified item ID from the request with the specified request ID in Firestore.
     *
     * @param requestId The ID of the request from which the item should be deleted.
     * @param itemId The ID of the item to be deleted.
     */
    suspend fun deleteItem(requestId: String, itemId: String) {
        collection
            .document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS)
            .document(itemId)
            .delete()
            .await()
    }


}


