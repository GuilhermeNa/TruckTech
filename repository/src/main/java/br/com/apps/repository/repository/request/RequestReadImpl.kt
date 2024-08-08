package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.model.request.PaymentRequest
import br.com.apps.model.model.request.RequestItem
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestReadImpl(private val fireStore: FirebaseFirestore) : RequestReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    override suspend fun fetchRequestListByDriverId(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<PaymentRequest>>> = withContext(Dispatchers.IO) {
        if (driverId.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRequestList() }
            false -> listener.onComplete { it.toRequestList() }
        }
    }

    override suspend fun fetchItemListByRequests(
        idList: List<String>,
        flow: Boolean
    ): LiveData<Response<List<RequestItem>>> = withContext(Dispatchers.IO) {
        if (idList.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id list cannot be empty")))

        val listener = fireStore.collectionGroup(FIRESTORE_COLLECTION_ITEMS)
            .whereIn(REQUEST_ID, idList)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRequestItemList() }
            false -> listener.onComplete { it.toRequestItemList() }
        }
    }

    override suspend fun fetchRequestById(
        requestId: String,
        flow: Boolean
    ): LiveData<Response<PaymentRequest>> = withContext(Dispatchers.IO) {
        if (requestId.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener = collection.document(requestId)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRequestObject() }
            false -> listener.onComplete { it.toRequestObject() }
        }
    }


    override suspend fun fetchItemListByRequestId(
        requestId: String,
        flow: Boolean
    ): LiveData<Response<List<RequestItem>>> = withContext(Dispatchers.IO) {
        if (requestId.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener = collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRequestItemList() }
            false -> listener.onComplete { it.toRequestItemList() }
        }
    }

    override suspend fun fetchItemById(
        requestId: String,
        itemId: String,
        flow: Boolean
    ): LiveData<Response<RequestItem>> = withContext(Dispatchers.IO) {
        if (requestId.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener = collection.document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS).document(itemId)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRequestItemObject() }
            false -> listener.onComplete { it.toRequestItemObject() }
        }
    }

}