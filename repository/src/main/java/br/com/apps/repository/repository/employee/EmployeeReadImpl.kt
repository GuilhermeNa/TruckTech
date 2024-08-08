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

    ///////////////////////////------------------------------------TODO

    /* override suspend fun fetchEmployeeListByMasterUid(
         masterUid: String,
         flow: Boolean
     ): LiveData<Response<List<Employee>>> {
         val mediator = MediatorLiveData<Response<List<Employee>>>()
         val dataSet = mutableListOf<Employee>()

         mediator.addSource(fetchAllDrivers(masterUid)) { response ->
             when (response) {
                 is Response.Error -> {}
                 is Response.Success -> {
                     response.data?.let {
                         dataSet.removeAll { driver -> driver is Driver }
                         dataSet.addAll(it)
                     }
                 }
             }
             mediator.value = Response.Success(data = dataSet)
         }
         mediator.addSource(getAllAdmins(masterUid)) { response ->
             when (response) {
                 is Response.Error -> {}
                 is Response.Success -> {
                     response.data?.let {
                         dataSet.removeAll { admin -> admin is Assistant }
                         dataSet.addAll(it)
                     }
                 }
             }
             mediator.value = Response.Success(data = dataSet)
         }

         return mediator
     }

     private suspend fun fetchAllDrivers(
         masterUid: String,
         flow: Boolean = false
     ): LiveData<Response<List<Employee>>> = withContext(Dispatchers.IO) {
         if (masterUid.isBlank())
             return@withContext MutableLiveData(Response.Error(EmptyDataException("MasterUid cannot be blank")))

         val listener = collectionDriver.whereEqualTo(MASTER_UID, masterUid)

         return@withContext if (flow) listener.onSnapShot { it.toEmployeeList() }
         else listener.onComplete { it.toEmployeeList() }
     }

     private suspend fun getAllAdmins(
         masterUid: String,
         flow: Boolean = false
     ): LiveData<Response<List<Employee>>> = withContext(Dispatchers.IO) {
         if (masterUid.isBlank())
             return@withContext MutableLiveData(Response.Error(EmptyDataException("MasterUid cannot be blank")))


         val listener = collectionAdmin.whereEqualTo(MASTER_UID, masterUid)

         return@withContext if (flow) listener.onSnapShot { it.toEmployeeList() }
         else listener.onComplete { it.toEmployeeList() }
     }

     override suspend fun fetchById(
         id: String,
         type: WorkRole,
         flow: Boolean
     ): LiveData<Response<Employee>> = withContext(Dispatchers.IO) {
         if (id.isBlank())
             return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be blank")))

         val collection = getCollectionReference(type)
         val listener = collection.document(id)

         return@withContext if (flow) listener.onSnapShot { it.toEmployeeObject() }
         else listener.onComplete { it.toEmployeeObject() }
     }

     override suspend fun getEmployeeBankAccounts(
         id: String,
         type: WorkRole,
         flow: Boolean
     ): LiveData<Response<List<BankAccount>>> = withContext(Dispatchers.IO) {
         if (id.isBlank())
             return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be blank")))

         val collection = getCollectionReference(type)
         val listener = collection.document(id).collection(FIRESTORE_COLLECTION_BANK)
             .orderBy(INSERTION_DATE, Query.Direction.DESCENDING)

         return@withContext if (flow) listener.onSnapShot { it.toBankAccountList() }
         else listener.onComplete { it.toBankAccountList() }
     }

     override suspend fun getBankAccountById(
         employeeId: String,
         bankId: String,
         type: WorkRole,
         flow: Boolean
     ): LiveData<Response<BankAccount>> = withContext(Dispatchers.IO) {
         if (employeeId.isBlank() || bankId.isBlank())
             return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be blank")))

         val collection = getCollectionReference(type)
         val listener = collection.document(employeeId)
             .collection(FIRESTORE_COLLECTION_BANK).document(bankId)

         return@withContext if (flow) listener.onSnapShot { it.toBankAccountObject() }
         else listener.onComplete { it.toBankAccountObject() }
     }

     private fun getCollectionReference(type: WorkRole): CollectionReference {
         val collection = when (type) {
             WorkRole.DRIVER -> collectionDriver
             WorkRole.ASSISTANT -> collectionAdmin
         }
         return collection
     }*/

}