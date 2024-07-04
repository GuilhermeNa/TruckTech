package br.com.apps.repository.repository.loan

import androidx.lifecycle.LiveData
import br.com.apps.model.model.payroll.Loan
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_LOANS
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toLoanList
import com.google.firebase.firestore.FirebaseFirestore

class LoanReadImpl(fireStore: FirebaseFirestore): LoanReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_LOANS)

    override suspend fun getLoanListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Loan>>> {
        val listener =
            collection.whereEqualTo(EMPLOYEE_ID, employeeId).whereEqualTo(IS_PAID, isPaid)
        return if (flow) listener.onSnapShot { it.toLoanList() }
        else listener.onComplete { it.toLoanList() }
    }

    override suspend fun getLoanListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Loan>>> {
        val listener =
            collection.whereIn(EMPLOYEE_ID, employeeIdList).whereEqualTo(IS_PAID, isPaid)
        return if (flow) listener.onSnapShot { it.toLoanList() }
        else listener.onComplete { it.toLoanList() }
    }

}

