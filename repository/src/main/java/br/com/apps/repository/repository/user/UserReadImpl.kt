package br.com.apps.repository.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.User
import br.com.apps.repository.util.FIRESTORE_COLLECTION_USERS
import br.com.apps.repository.util.LAST_REQUEST
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toUserObject
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserReadImpl(firestore: FirebaseFirestore) : UserReadInterface {

    private val collection = firestore.collection(FIRESTORE_COLLECTION_USERS)

    override suspend fun fetchById(
        uid: String, flow: Boolean
    ): LiveData<Response<User>> = withContext(Dispatchers.IO) {
        uid.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(uid)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toUserObject() }
            false -> listener.onComplete { it.toUserObject() }
        }
    }

    override suspend fun updateRequestNumber(uid: String) {
        collection
            .document(uid)
            .update(LAST_REQUEST, FieldValue.increment(1))
            .await()
    }

    override suspend fun getUserRequestNumber(uid: String): Long {
        val doc = collection.document(uid).get().await()
        return doc.toUserObject().lastRequest
    }

}