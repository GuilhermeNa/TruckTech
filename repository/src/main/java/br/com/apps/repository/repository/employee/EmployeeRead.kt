package br.com.apps.repository.repository.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.employee_dto.AdminEmployeeDto
import br.com.apps.model.dto.employee_dto.DriverEmployeeDto
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.Admin
import br.com.apps.model.model.employee.Driver
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.Resource
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ADMIN
import br.com.apps.repository.util.FIRESTORE_COLLECTION_BANK
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DRIVER
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toBankAccountObject
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmployeeRead(fireStore: FirebaseFirestore): EmployeeReadI {

    private val collectionDriver = fireStore.collection(FIRESTORE_COLLECTION_DRIVER)
    private val collectionAdmin = fireStore.collection(FIRESTORE_COLLECTION_ADMIN)

    /**
     *  Get All employees
     */
    override fun getAll(masterUid: String): MutableLiveData<Resource<List<Employee>>> {
        val mediator = MediatorLiveData<Resource<List<Employee>>>()
        val dataSet = mutableListOf<Employee>()

        mediator.addSource(getAllDrivers(masterUid)) {
            it?.let { resource ->
                dataSet.removeAll { driver -> driver is Driver }
                dataSet.addAll(resource.data)
            }
            mediator.value = Resource(data = dataSet)
        }
        mediator.addSource(getAllAdmins(masterUid)) {
            it?.let { resource ->
                dataSet.removeAll { admin -> admin is Admin }
                dataSet.addAll(resource.data)
            }
            mediator.value = Resource(data = dataSet)
        }

        return mediator
    }

    private fun getAllDrivers(masterUid: String): MutableLiveData<Resource<List<Employee>>> {
        val liveData = MutableLiveData<Resource<List<Employee>>>()

        collectionDriver.whereEqualTo(MASTER_UID, masterUid)
            .addSnapshotListener { s, _ ->
                s?.let {
                    val resource = querySnapShotMapper<DriverEmployeeDto>(it)
                    liveData.value = resource
                }
            }

        return liveData
    }

    private fun getAllAdmins(masterUid: String): MutableLiveData<Resource<List<Employee>>> {
        val liveData = MutableLiveData<Resource<List<Employee>>>()

        collectionAdmin.whereEqualTo(MASTER_UID, masterUid)
            .addSnapshotListener { s, _ ->
                s?.let {
                    val resource = querySnapShotMapper<AdminEmployeeDto>(it)
                    liveData.value = resource
                }
            }

        return liveData
    }

    /**
     * get by id
     */
    override fun getById(id: String, type: EmployeeType): LiveData<Resource<Employee>> {
        return when (type) {
            EmployeeType.DRIVER -> getDriver(id)
            EmployeeType.ADMIN -> getAdmin(id)
        }
    }

    private fun getAdmin(id: String): MutableLiveData<Resource<Employee>> {
        val liveData = MutableLiveData<Resource<Employee>>()

        collectionAdmin.document(id)
            .addSnapshotListener { s, _ ->
                s?.let { document ->
                    document.toObject<AdminEmployeeDto>()?.let { dto ->
                        dto.toModel()
                    }?.let {
                        liveData.value = Resource(data = it)
                    }
                }
            }

        return liveData
    }

    private fun getDriver(id: String): MutableLiveData<Resource<Employee>> {
        val liveData = MutableLiveData<Resource<Employee>>()

        collectionDriver.document(id)
            .addSnapshotListener { s, _ ->
                s?.let { document ->
                    document.toObject<DriverEmployeeDto>()?.let { dto ->
                        dto.toModel()
                    }?.let {
                        liveData.value = Resource(data = it)
                    }
                }
            }

        return liveData
    }

    /**
     * Get employee bank accounts
     */
    override suspend fun getEmployeeBankAccounts(
        id: String,
        type: EmployeeType
    ): LiveData<Response<List<BankAccount>>> {
        return when (type) {
            EmployeeType.DRIVER -> getDriverBankAccounts(id)
            EmployeeType.ADMIN -> getAdminBankAccounts(id)
        }
    }

    private suspend fun getDriverBankAccounts(id: String): LiveData<Response<List<BankAccount>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<BankAccount>>>()

            collectionDriver.document(id).addSnapshotListener { docSnap, error ->
                error?.let { e ->
                    liveData.postValue(Response.Error(e))
                }
                docSnap?.let { document ->
                    document.reference.collection(FIRESTORE_COLLECTION_BANK)
                        .addSnapshotListener { querySnap, error ->
                            error?.let { e ->
                                Response.Error(e)
                            }
                            liveData.postValue(Response.Success(data = querySnap?.toBankList()))
                        }
                }
            }

            return@withContext liveData
        }
    }

    private suspend fun getAdminBankAccounts(id: String): LiveData<Response<List<BankAccount>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<BankAccount>>>()

            collectionAdmin.document(id).addSnapshotListener { docSnap, error ->
                error?.let { e ->
                    liveData.postValue(Response.Error(e))
                }
                docSnap?.let { document ->
                    document.reference.collection(FIRESTORE_COLLECTION_BANK)
                        .addSnapshotListener { querySnap, error ->
                            error?.let { e ->
                                liveData.postValue(Response.Error(e))
                            }
                            liveData.postValue(Response.Success(data = querySnap?.toBankList()))
                        }
                }
            }

            return@withContext liveData
        }
    }

    /**
     * Get employee bank accounts
     */
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