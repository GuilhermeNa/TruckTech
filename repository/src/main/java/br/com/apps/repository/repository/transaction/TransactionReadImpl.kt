package br.com.apps.repository.repository.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.finance.Transaction
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRANSACTIONS
import br.com.apps.repository.util.PARENT_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTransactionList
import br.com.apps.repository.util.toTransactionObject
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionReadImpl(private val firestore: FirebaseFirestore) : TransactionReadInterface {

    private val collection = firestore.collection(FIRESTORE_COLLECTION_TRANSACTIONS)

    override suspend fun fetchTransactionsByParentId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Transaction>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(PARENT_ID, id)

        return@withContext when(flow) {
            true -> listener.onSnapShot { it.toTransactionList() }
            false -> listener.onComplete { it.toTransactionList() }
        }
    }

    override suspend fun fetchTransactionById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Transaction>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when(flow) {
            true -> listener.onSnapShot { it.toTransactionObject() }
            false -> listener.onComplete { it.toTransactionObject() }
        }
    }

    override suspend fun fetchTransactionsByParentIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Transaction>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(PARENT_ID, ids)

        return@withContext when(flow) {
            true -> listener.onSnapShot { it.toTransactionList() }
            false -> listener.onComplete { it.toTransactionList() }
        }
    }

}