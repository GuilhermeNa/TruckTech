package br.com.apps.repository.repository.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.Admin
import br.com.apps.model.model.employee.Driver
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.employee.EmployeeType
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

class EmployeeReadImpl(fireStore: FirebaseFirestore) : EmployeeReadInterface {

    private val collectionDriver = fireStore.collection(FIRESTORE_COLLECTION_DRIVER)
    private val collectionAdmin = fireStore.collection(FIRESTORE_COLLECTION_ADMIN)

    override suspend fun getEmployeeListByMasterUid(masterUid: String, flow: Boolean)
            : LiveData<Response<List<Employee>>> {
        val mediator = MediatorLiveData<Response<List<Employee>>>()
        val dataSet = mutableListOf<Employee>()

        mediator.addSource(getAllDrivers(masterUid)) { response ->
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

    private suspend fun getAllDrivers(masterUid: String, flow: Boolean = false)
            : LiveData<Response<List<Employee>>> {
        val listener = collectionDriver.whereEqualTo(MASTER_UID, masterUid)

        return if (flow) listener.onSnapShot { it.toEmployeeList() }
        else listener.onComplete { it.toEmployeeList() }
    }

    private suspend fun getAllAdmins(masterUid: String, flow: Boolean = false)
            : LiveData<Response<List<Employee>>> {
        val listener = collectionAdmin.whereEqualTo(MASTER_UID, masterUid)

        return if (flow) listener.onSnapShot { it.toEmployeeList() }
        else listener.onComplete { it.toEmployeeList() }
    }

    override suspend fun getById(id: String, type: EmployeeType, flow: Boolean)
            : LiveData<Response<Employee>> {
        val collection = getCollectionReference(type)
        val listener = collection.document(id)

        return if (flow) listener.onSnapShot { it.toEmployeeObject() }
        else listener.onComplete { it.toEmployeeObject() }
    }

    override suspend fun getEmployeeBankAccounts(id: String, type: EmployeeType, flow: Boolean)
            : LiveData<Response<List<BankAccount>>> {
        val collection = getCollectionReference(type)
        val listener = collection.document(id).collection(FIRESTORE_COLLECTION_BANK)
            .orderBy(INSERTION_DATE, Query.Direction.DESCENDING)

        return if (flow) listener.onSnapShot { it.toBankAccountList() }
        else listener.onComplete { it.toBankAccountList() }
    }

    override suspend fun getBankAccountById(
        employeeId: String,
        bankId: String,
        type: EmployeeType,
        flow: Boolean
    ): LiveData<Response<BankAccount>> {
        val collection = getCollectionReference(type)
        val listener = collection.document(employeeId)
            .collection(FIRESTORE_COLLECTION_BANK).document(bankId)

        return if (flow) listener.onSnapShot { it.toBankAccountObject() }
        else listener.onComplete { it.toBankAccountObject() }
    }

    private fun getCollectionReference(type: EmployeeType): CollectionReference {
        val collection = when (type) {
            EmployeeType.DRIVER -> collectionDriver
            EmployeeType.ADMIN -> collectionAdmin
        }
        return collection
    }

}