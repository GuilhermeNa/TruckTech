package br.com.apps.repository.repository.loan

import androidx.lifecycle.LiveData
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.payroll.Loan
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_LOANS
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toLoanList
import com.google.firebase.firestore.FirebaseFirestore

class LoanRead(fireStore: FirebaseFirestore): LoanReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_LOANS)

    /**
     * Fetches the [Loan] dataSet for the specified employee ID.
     *
     * @param employeeId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Loan] list.
     */
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

    /**
     * Fetches the [Loan] dataSet for the specified employee ID list.
     *
     * @param employeeIdList The ID of the [Employee]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Loan] list.
     */
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

