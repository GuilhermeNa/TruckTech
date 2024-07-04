package br.com.apps.repository.repository.customer

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Customer
import br.com.apps.repository.util.FIRESTORE_COLLECTION_CUSTOMER
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toCustomerList
import br.com.apps.repository.util.toCustomerObject
import com.google.firebase.firestore.FirebaseFirestore

class CustomerReadImpl(fireStore: FirebaseFirestore): CustomerReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_CUSTOMER)

    override suspend fun getCustomerListByMasterUid(masterUid: String, flow: Boolean)
    : LiveData<Response<List<Customer>>> {
        val listener = collection.whereEqualTo(MASTER_UID, masterUid)
        return if(flow) listener.onSnapShot { it.toCustomerList() }
        else listener.onComplete { it.toCustomerList() }
    }

    override suspend fun getCustomerById(id: String, flow: Boolean): LiveData<Response<Customer>> {
        val listener = collection.document(id)
        return if(flow) listener.onSnapShot { it.toCustomerObject() }
        else listener.onComplete { it.toCustomerObject() }
    }

}