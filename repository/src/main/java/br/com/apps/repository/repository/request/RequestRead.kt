package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.repository.util.DRIVER_ID
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

class RequestRead(private val fireStore: FirebaseFirestore) : RequestReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    /**
     * Fetches the [PaymentRequest] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    override suspend fun getRequestListByDriverId(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<PaymentRequest>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return if (flow) listener.onSnapShot { it.toRequestList() }
        else listener.onComplete { it.toRequestList() }
    }

    /**
     * Fetches the [RequestItem] dataSet for the specified request's ID.
     *
     * @param idList The ID's of the [PaymentRequest]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [RequestItem] list.
     */
    override suspend fun getItemListByRequests(
        idList: List<String>,
        flow: Boolean
    ): LiveData<Response<List<RequestItem>>> {
        if (idList.isEmpty()) return MutableLiveData(Response.Success(emptyList()))

        val listener = fireStore.collectionGroup(FIRESTORE_COLLECTION_ITEMS)
            .whereIn(REQUEST_ID, idList)

        return if (flow) listener.onSnapShot { it.toRequestItemList() }
        else listener.onComplete { it.toRequestItemList() }
    }

    /**
     * Fetches the [PaymentRequest] dataSet for the specified ID.
     *
     * @param requestId The ID of the [PaymentRequest].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    override suspend fun getRequestById(
        requestId: String,
        flow: Boolean
    ): LiveData<Response<PaymentRequest>> {
        val listener = collection.document(requestId)

        return if (flow) listener.onSnapShot { it.toRequestObject() }
        else listener.onComplete { it.toRequestObject() }
    }

    /**
     * Fetches the [RequestItem] dataSet for the specified request ID.
     *
     * @param requestId The ID of the [PaymentRequest].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [RequestItem] list.
     */
    override suspend fun getItemListByRequestId(
        requestId: String,
        flow: Boolean
    ): LiveData<Response<List<RequestItem>>> {
        val listener = collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS)

        return if (flow) listener.onSnapShot { it.toRequestItemList() }
        else listener.onComplete { it.toRequestItemList() }
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

        return if (flow) listener.onSnapShot { it.toRequestItemObject() }
        else listener.onComplete { it.toRequestItemObject() }
    }

}