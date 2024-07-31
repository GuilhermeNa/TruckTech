package br.com.apps.repository.repository.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.model.payroll.Loan
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_LOANS
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toLoanList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoanReadImpl(fireStore: FirebaseFirestore): LoanReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_LOANS)

    override suspend fun fetchLoanListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Loan>>> = withContext(Dispatchers.IO) {
        if (employeeId.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener =
            collection.whereEqualTo(EMPLOYEE_ID, employeeId).whereEqualTo(IS_PAID, isPaid)

        return@withContext if (flow) listener.onSnapShot { it.toLoanList() }
        else listener.onComplete { it.toLoanList() }
    }

    override suspend fun fetchLoanListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Loan>>> = withContext(Dispatchers.IO) {
        if (employeeIdList.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id list cannot be empty")))

        val listener =
            collection.whereIn(EMPLOYEE_ID, employeeIdList).whereEqualTo(IS_PAID, isPaid)

        return@withContext if (flow) listener.onSnapShot { it.toLoanList() }
        else listener.onComplete { it.toLoanList() }
    }

}

