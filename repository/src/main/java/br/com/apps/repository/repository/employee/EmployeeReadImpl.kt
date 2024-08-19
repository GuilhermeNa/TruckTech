package br.com.apps.repository.repository.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.enums.WorkRole
import br.com.apps.model.model.employee.Employee
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EMPLOYEE
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TYPE
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toEmployeeList
import br.com.apps.repository.util.toEmployeeObject
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmployeeReadImpl(fireStore: FirebaseFirestore) : EmployeeReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EMPLOYEE)

    override suspend fun fetchById(
        id: String, flow: Boolean
    ): LiveData<Response<Employee>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toEmployeeObject() }
            false -> listener.onSnapShot { it.toEmployeeObject() }
        }
    }

    override suspend fun fetchEmployeesByMasterUidAndRole(
        masterUid: String, role: WorkRole, flow: Boolean
    ): LiveData<Response<List<Employee>>> = withContext(Dispatchers.IO) {
        masterUid.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(MASTER_UID, masterUid).whereEqualTo(TYPE, role.name)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toEmployeeList() }
            false -> listener.onSnapShot { it.toEmployeeList() }
        }
    }

}