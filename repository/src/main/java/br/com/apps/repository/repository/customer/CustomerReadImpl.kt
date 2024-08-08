package br.com.apps.repository.repository.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.Customer
import br.com.apps.repository.util.FIRESTORE_COLLECTION_CUSTOMER
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toCustomerList
import br.com.apps.repository.util.toCustomerObject
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomerReadImpl(fireStore: FirebaseFirestore) : CustomerReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_CUSTOMER)

    override suspend fun fetchCustomerListByMasterUid(
        uid: String,
        flow: Boolean
    ): LiveData<Response<List<Customer>>> = withContext(Dispatchers.IO) {
        uid.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(MASTER_UID, uid)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toCustomerList() }
            false -> listener.onComplete { it.toCustomerList() }
        }
    }

    override suspend fun fetchCustomerById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Customer>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toCustomerObject() }
            false -> listener.onComplete { it.toCustomerObject() }
        }
    }

}