package br.com.apps.repository.repository.payable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.model.finance.payable.Payable
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EMPLOYEE_PAYABLE
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.PARENT_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TYPE
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toPayableList
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmployeePayableReadImpl(fireStore: FirebaseFirestore): EmployeePayableReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EMPLOYEE_PAYABLE)

    override suspend fun fetchPayablesByParentId(
        id: String,
        type: EmployeePayableTicket,
        flow: Boolean
    ): LiveData<Response<List<Payable>>> = withContext(Dispatchers.IO){
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(PARENT_ID, id).whereEqualTo(TYPE, type.name)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toPayableList() }
            false -> listener.onComplete { it.toPayableList() }
        }
    }

    override suspend fun fetchPayablesByEmployeeIdAndStatus(
        id: String,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Payable>>> = withContext(Dispatchers.IO){
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id).whereEqualTo(IS_PAID, isPaid)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toPayableList() }
            false -> listener.onComplete { it.toPayableList() }
        }
    }


}