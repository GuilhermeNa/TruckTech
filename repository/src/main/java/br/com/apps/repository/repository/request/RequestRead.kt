package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ITEMS
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REQUESTS
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
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

private const val EMPTY_ID_LIST = "Empty id list"

@OptIn(ExperimentalCoroutinesApi::class)
class RequestRead(private val fireStore: FirebaseFirestore): RequestReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    /**
     * Fetches the [PaymentRequest] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    override suspend fun getCompleteRequestListByDriverId(driverId: String, flow: Boolean)
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
                            if (query.isEmpty) {
                                liveData.postValue(Response.Error(InvalidParameterException(EMPTY_ID_LIST)))
                            } else {
                                val requestList = query.toRequestList()
                                deferred.complete(requestList)
                            }
                        }
                    }
            } else {
                collection.whereEqualTo(DRIVER_ID, driverId).get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        if(query.isEmpty) {
                            liveData.postValue(Response.Error(InvalidParameterException(EMPTY_ID_LIST)))
                        } else {
                            val requestList = query.toRequestList()
                            deferred.complete(requestList)
                        }
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
    override suspend fun getCompleteRequestById(
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
    override suspend fun getItemById(
        requestId: String,
        itemId: String,
        flow: Boolean
    ): LiveData<Response<RequestItem>> {
        val listener = collection.document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS).document(itemId)

        return if(flow) listener.onSnapShot { it.toRequestItemObject() }
        else listener.onComplete { it.toRequestItemObject() }
    }

}