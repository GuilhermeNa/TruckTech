package br.com.apps.repository.repository.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.request.Item
import br.com.apps.repository.util.DATE
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ITEMS
import br.com.apps.repository.util.PARENT_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toItemList
import br.com.apps.repository.util.toItemObject
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemReadImpl(firestore: FirebaseFirestore): ItemReadInterface {

    private val collection = firestore.collection(FIRESTORE_COLLECTION_ITEMS)

    override suspend fun fetchItemById(
        id: String, flow: Boolean
    ): LiveData<Response<Item>> = withContext(Dispatchers.IO){
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when(flow) {
            true -> listener.onSnapShot { it.toItemObject() }
            false -> listener.onComplete { it.toItemObject() }
        }
    }

    override suspend fun fetchItemsByParentId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Item>>> = withContext(Dispatchers.IO){
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(PARENT_ID, id)

        return@withContext when(flow) {
            true -> listener.onSnapShot { it.toItemList() }
            false -> listener.onComplete { it.toItemList() }
        }
    }

    override suspend fun fetchItemsByParentIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Item>>> = withContext(Dispatchers.IO){
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(PARENT_ID, ids)

        return@withContext when(flow){
            true -> listener.onSnapShot { it.toItemList() }
            false -> listener.onComplete { it.toItemList() }
        }
    }

    override suspend fun fetchItemsByParentIdAndDateDesc(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Item>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(PARENT_ID, id)
            .orderBy(DATE, Query.Direction.DESCENDING)


        return@withContext when(flow) {
            true -> listener.onSnapShot { it.toItemList() }
            false -> listener.onComplete { it.toItemList() }
        }
    }

}