package br.com.apps.repository.repository.receivable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.model.finance.receivable.Receivable
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EMPLOYEE_RECEIVABLE
import br.com.apps.repository.util.IS_RECEIVED
import br.com.apps.repository.util.PARENT_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TYPE
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toReceivableList
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmployeeReceivableReadImpl(fireStore: FirebaseFirestore) : EmployeeReceivableReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EMPLOYEE_RECEIVABLE)

    override suspend fun fetchReceivablesByParentId(
        id: String,
        type: EmployeeReceivableTicket,
        flow: Boolean
    ): LiveData<Response<List<Receivable>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(PARENT_ID, id).whereEqualTo(TYPE, type.name)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toReceivableList() }
            false -> listener.onComplete { it.toReceivableList() }
        }
    }

    override suspend fun fetchReceivableByEmployeeIdAndStatus(
        id: String,
        isReceived: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Receivable>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id).whereEqualTo(IS_RECEIVED, isReceived)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toReceivableList() }
            false -> listener.onComplete { it.toReceivableList() }
        }
    }

}