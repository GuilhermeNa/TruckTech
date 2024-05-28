package br.com.apps.repository.repository.customer

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Customer
import br.com.apps.repository.util.FIRESTORE_COLLECTION_CUSTOMER
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toCustomerList
import com.google.firebase.firestore.FirebaseFirestore

class CustomerRead(fireStore: FirebaseFirestore): CustomerReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_CUSTOMER)

    override suspend fun getCustomerListByMasterUid(masterUid: String, flow: Boolean)
    : LiveData<Response<List<Customer>>> {
        val listener = collection.whereEqualTo(MASTER_UID, masterUid)

        return if(flow) listener.onSnapShot { it.toCustomerList() }
        else listener.onComplete { it.toCustomerList() }
    }



}