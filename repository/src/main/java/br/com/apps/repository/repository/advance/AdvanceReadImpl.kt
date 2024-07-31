package br.com.apps.repository.repository.advance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.model.payroll.Advance
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ADVANCES
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toAdvanceList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdvanceReadImpl(fireStore: FirebaseFirestore) : AdvanceReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_ADVANCES)

    override suspend fun fetchAdvanceListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Advance>>> = withContext(Dispatchers.IO) {
        if (employeeId.isBlank())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be blank")))

        val listener =
            collection.whereEqualTo(EMPLOYEE_ID, employeeId).whereEqualTo(IS_PAID, isPaid)

        return@withContext if (flow) listener.onSnapShot { it.toAdvanceList() }
        else listener.onComplete { it.toAdvanceList() }
    }

    override suspend fun fetchAdvanceListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Advance>>> = withContext(Dispatchers.IO) {
        if (employeeIdList.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("IdList cannot be empty")))

        val listener = collection.whereIn(EMPLOYEE_ID, employeeIdList).whereEqualTo(IS_PAID, isPaid)

        return@withContext if (flow) listener.onSnapShot { it.toAdvanceList() }
        else listener.onComplete { it.toAdvanceList() }
    }

}