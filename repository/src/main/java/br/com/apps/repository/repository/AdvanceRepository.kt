package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.payroll.Advance
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ADVANCES
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.toAdvanceList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AdvanceRepository(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_ADVANCES)

    /**
     * This function fetches [Advance] dataSet for the given [Employee] ID asynchronously from the database.
     *
     * @param employeeId The ID of the [Employee] for which [Advance] dataSet is to be retrieved.
     * @param withFlow If the user wants to keep observing the source or not.
     * @param isPaid Whether the advance has already been paid or not.
     *
     * @return A LiveData object containing a [Response] of [Advance] dataSet for the specified ID.
     */
    suspend fun getAdvanceListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        withFlow: Boolean
    ): LiveData<Response<List<Advance>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Advance>>>()
            val listener = collection
                .whereEqualTo(EMPLOYEE_ID, employeeId)
                .whereEqualTo(IS_PAID, isPaid)

            if (withFlow) {
                listener.addSnapshotListener { snapQuery, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    snapQuery?.let { query ->
                        liveData.postValue(Response.Success(query.toAdvanceList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(query.toAdvanceList()))
                    }
                }.await()
            }

            return@withContext liveData
        }
    }

    /**
     * This function fetches [Advance] dataSet for the given [Employee] ID asynchronously from the database.
     *
     * @param employeeIdList The ID list of the [Employee] for which [Advance] dataSet is to be retrieved.
     * @param isPaid Whether the advance has already been paid or not.
     *
     * @return A LiveData object containing a [Response] of [Advance] dataSet for the specified IDs in the list.
     */
    suspend fun getAdvanceListByEmployeeIdAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean
    ): LiveData<Response<List<Advance>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Advance>>>()
            val listener = collection
                .whereIn(EMPLOYEE_ID, employeeIdList)
                .whereEqualTo(IS_PAID, isPaid)

            listener.get().addOnCompleteListener { task ->
                task.exception?.let { e ->
                    liveData.postValue(Response.Error(e))
                }
                task.result?.let { query ->
                    liveData.postValue(Response.Success(query.toAdvanceList()))
                }
            }.await()

            return@withContext liveData
        }
    }

}