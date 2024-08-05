package br.com.apps.repository.repository.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.enums.WorkRole
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.Admin
import br.com.apps.model.model.employee.Driver
import br.com.apps.model.model.employee.Employee
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ADMIN
import br.com.apps.repository.util.FIRESTORE_COLLECTION_BANK
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DRIVER
import br.com.apps.repository.util.INSERTION_DATE
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toBankAccountList
import br.com.apps.repository.util.toBankAccountObject
import br.com.apps.repository.util.toEmployeeList
import br.com.apps.repository.util.toEmployeeObject
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmployeeReadImpl(fireStore: FirebaseFirestore) : EmployeeReadInterface {

    private val collectionDriver = fireStore.collection(FIRESTORE_COLLECTION_DRIVER)
    private val collectionAdmin = fireStore.collection(FIRESTORE_COLLECTION_ADMIN)

    override suspend fun fetchEmployeeListByMasterUid(masterUid: String, flow: Boolean): LiveData<Response<List<Employee>>> {
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
                        dataSet.removeAll { admin -> admin is Admin }
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
    ) : LiveData<Response<Employee>> = withContext(Dispatchers.IO) {
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
    ) : LiveData<Response<List<BankAccount>>> = withContext(Dispatchers.IO) {
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
            WorkRole.TRUCK_DRIVER -> collectionDriver
            WorkRole.ADMIN -> collectionAdmin
        }
        return collection
    }

}