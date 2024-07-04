package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class RequestReadImpl(private val fireStore: FirebaseFirestore) : RequestReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    override suspend fun getRequestListByDriverId(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<PaymentRequest>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)
        return if (flow) listener.onSnapShot { it.toRequestList() }
        else listener.onComplete { it.toRequestList() }
    }

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

    override suspend fun getRequestById(
        requestId: String,
        flow: Boolean
    ): LiveData<Response<PaymentRequest>> {
        val listener = collection.document(requestId)
        return if (flow) listener.onSnapShot { it.toRequestObject() }
        else listener.onComplete { it.toRequestObject() }
    }


    override suspend fun getItemListByRequestId(
        requestId: String,
        flow: Boolean
    ): LiveData<Response<List<RequestItem>>> {
        val listener = collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS)
        return if (flow) listener.onSnapShot { it.toRequestItemList() }
        else listener.onComplete { it.toRequestItemList() }
    }

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