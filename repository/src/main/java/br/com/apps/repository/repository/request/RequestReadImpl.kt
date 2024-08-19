package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.model.request.Request
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REQUESTS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.USER_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toRequestList
import br.com.apps.repository.util.toRequestObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestReadImpl(fireStore: FirebaseFirestore) : RequestReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    override suspend fun fetchRequestListByUid(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Request>>> = withContext(Dispatchers.IO) {
        if (id.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener = collection.whereEqualTo(USER_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRequestList() }
            false -> listener.onComplete { it.toRequestList() }
        }
    }

    override suspend fun fetchRequestById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Request>> = withContext(Dispatchers.IO) {
        if (id.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRequestObject() }
            false -> listener.onComplete { it.toRequestObject() }
        }
    }

}