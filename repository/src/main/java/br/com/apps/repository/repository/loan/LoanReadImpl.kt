package br.com.apps.repository.repository.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.payroll.Loan
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_LOANS
import br.com.apps.repository.util.ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toLoanList
import br.com.apps.repository.util.toLoanObject
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoanReadImpl(fireStore: FirebaseFirestore) : LoanReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_LOANS)

    override suspend fun fetchLoanListByEmployeeId(
        id: String, flow: Boolean
    ): LiveData<Response<List<Loan>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toLoanList() }
            false -> listener.onComplete { it.toLoanList() }
        }
    }

    override suspend fun fetchLoanListByEmployeeIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Loan>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(EMPLOYEE_ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toLoanList() }
            false -> listener.onComplete { it.toLoanList() }
        }
    }

    override suspend fun fetchLoanById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Loan>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toLoanObject() }
            false -> listener.onComplete { it.toLoanObject() }
        }
    }

    override suspend fun fetchLoanByIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Loan>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> MutableLiveData(error) }

        val listener = collection.whereIn(ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toLoanList() }
            false -> listener.onComplete { it.toLoanList() }
        }
    }

}

