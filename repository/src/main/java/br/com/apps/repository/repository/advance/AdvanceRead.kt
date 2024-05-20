package br.com.apps.repository.repository.advance

import androidx.lifecycle.LiveData
import br.com.apps.model.model.payroll.Advance
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ADVANCES
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toAdvanceList
import com.google.firebase.firestore.FirebaseFirestore

class AdvanceRead(fireStore: FirebaseFirestore): AdvanceReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_ADVANCES)

    /**
     * Fetches the [Advance] dataSet for the specified employee ID.
     *
     * @param employeeId The ID of the [Employee].
     * @param isPaid if the [Advance] is already paid.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Advance] list.
     */
    override suspend fun getAdvanceListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Advance>>> {
        val listener = collection
            .whereEqualTo(EMPLOYEE_ID, employeeId)
            .whereEqualTo(IS_PAID, isPaid)

        return if (flow) listener.onSnapShot { it.toAdvanceList() }
        else listener.onComplete { it.toAdvanceList() }
    }

    /**
     * Fetches the [Advance] dataSet for the specified employee ID list.
     *
     * @param employeeIdList The ID list of the [Employee]'s.
     * @param isPaid if the [Advance] is already paid.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Advance] list.
     */
    override suspend fun getAdvanceListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Advance>>> {
        val listener = collection
            .whereIn(EMPLOYEE_ID, employeeIdList)
            .whereEqualTo(IS_PAID, isPaid)

        return if (flow) listener.onSnapShot { it.toAdvanceList() }
        else listener.onComplete { it.toAdvanceList() }
    }

}