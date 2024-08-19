package br.com.apps.repository.repository.document

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.document.TruckDocument
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DOCUMENTS
import br.com.apps.repository.util.FLEET_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toDocumentList
import br.com.apps.repository.util.toDocumentObject
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DocumentReadImpl(fireStore: FirebaseFirestore) : DocumentReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_DOCUMENTS)

    override suspend fun fetchDocumentById(
        id: String,
        flow: Boolean
    ): LiveData<Response<TruckDocument>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toDocumentObject() }
            false -> listener.onComplete { it.toDocumentObject() }
        }
    }

    override suspend fun fetchDocumentListByFleetId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<TruckDocument>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(FLEET_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toDocumentList() }
            false -> listener.onComplete { it.toDocumentList() }
        }
    }

    override suspend fun fetchDocumentListByFleetIdList(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<TruckDocument>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(FLEET_ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toDocumentList() }
            false -> listener.onComplete { it.toDocumentList() }
        }
    }

}
