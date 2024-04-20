package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.employee_dto.AdminEmployeeDto
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.DriverEmployeeDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.mapper.EmployeeMapper.Companion.toModel
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.employee.AdminEmployee
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.employee.DriverEmployee
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.MASTER_UID
import br.com.apps.repository.Resource
import br.com.apps.repository.Response
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

private const val FIRESTORE_COLLECTION_DRIVER = "driverEmployee"
private const val FIRESTORE_COLLECTION_ADMIN = "adminEmployee"
private const val FIRESTORE_COLLECTION_BANK = "bankAccount"

class EmployeeRepository(fireStore: FirebaseFirestore) {

    private val collectionDriver = fireStore.collection(FIRESTORE_COLLECTION_DRIVER)
    private val collectionAdmin = fireStore.collection(FIRESTORE_COLLECTION_ADMIN)

    /**
     * Add or save a dto
     */
    fun save(dto: EmployeeDto): String {
        return when (dto) {
            is DriverEmployeeDto -> addToCollection(dto, collectionDriver)
            is AdminEmployeeDto -> addToCollection(dto, collectionAdmin)
            else -> throw InvalidParameterException()
        }
    }

    private fun addToCollection(dto: EmployeeDto, collection: CollectionReference): String {
        val document =
            if (dto.id != null) {
                collection.document(dto.id!!)
            } else {
                collection.document().also {
                    dto.id = it.id
                }
            }
        document.set(dto)
        return document.id
    }

    /**
     *  Get All employees
     */
    fun getAll(masterUid: String): MutableLiveData<Resource<List<Employee>>> {
        val mediator = MediatorLiveData<Resource<List<Employee>>>()
        val dataSet = mutableListOf<Employee>()

        mediator.addSource(getAllDrivers(masterUid)) {
            it?.let { resource ->
                dataSet.removeAll { driver -> driver is DriverEmployee }
                dataSet.addAll(resource.data)
            }
            mediator.value = Resource(data = dataSet)
        }
        mediator.addSource(getAllAdmins(masterUid)) {
            it?.let { resource ->
                dataSet.removeAll { admin -> admin is AdminEmployee }
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
    fun getById(id: String, type: EmployeeType): LiveData<Resource<Employee>> {
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
     * Delete
     */
    fun deleteEmployee(id: String, type: EmployeeType): LiveData<Resource<Boolean>> {
        return when (type) {
            EmployeeType.DRIVER -> deleteDriver(id)
            EmployeeType.ADMIN -> deleteAdmin(id)
        }
    }

    private fun deleteDriver(id: String): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        collectionDriver.document(id).delete()
        liveData.value = Resource(data = true)
        return liveData
    }

    private fun deleteAdmin(id: String): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        collectionAdmin.document(id).delete()
        liveData.value = Resource(data = true)
        return liveData
    }

    /**
     * Get employee bank accounts
     */
    suspend fun getEmployeeBankAccounts(
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
     * Get bank account by id
     */
    suspend fun getBankAccountById(
        employeeId: String,
        bankId: String,
        type: EmployeeType
    ): LiveData<Response<BankAccount>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<BankAccount>>()

            val collection = when (type) {
                EmployeeType.DRIVER -> collectionDriver
                EmployeeType.ADMIN -> collectionAdmin
            }

            collection
                .document(employeeId)
                .collection(FIRESTORE_COLLECTION_BANK)
                .document(bankId)
                .addSnapshotListener { docSnap, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    docSnap?.let { document ->
                        liveData.postValue(Response.Success(data = document.toBankObject()))
                    }
                }

            return@withContext liveData
        }
    }

    /**
     * Delete employee Bank acccount
     */
    suspend fun deleteBankAccount(employeeId: String, bankId: String, type: EmployeeType) {
        val collection = when (type) {
            EmployeeType.DRIVER -> collectionDriver
            EmployeeType.ADMIN -> collectionAdmin
        }

        collection
            .document(employeeId)
            .collection(FIRESTORE_COLLECTION_BANK)
            .document(bankId)
            .delete()
            .await()

    }

    /**
     * Save bank account
     */
    suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: EmployeeType) {
        val collection = getCollectionReference(type)

        if (bankAccDto.id == null) {
            addNewBankAccount(bankAccDto, collection)
        } else {
            editBankAccount(bankAccDto, collection)
        }

    }

    private suspend fun editBankAccount(
        bankAccDto: BankAccountDto,
        collection: CollectionReference
    ) {
        val employeeId = bankAccDto.employeeId ?: throw IllegalArgumentException(EMPTY_ID)
        val id = bankAccDto.id ?: throw IllegalArgumentException(EMPTY_ID)

        val document =
            collection
                .document(employeeId)
                .collection(FIRESTORE_COLLECTION_BANK)
                .document(id)

        document.set(bankAccDto).await()
    }

    private suspend fun addNewBankAccount(
        bankAccDto: BankAccountDto,
        collection: CollectionReference
    ) {
        val employeeId = bankAccDto.employeeId ?: throw IllegalArgumentException("Empty id")

        val document =
            collection
                .document(employeeId)
                .collection(FIRESTORE_COLLECTION_BANK)
                .document()

        bankAccDto.id = document.id
        document.set(bankAccDto).await()
    }

    /**
     * Update main acc
     */
    suspend fun updateMainAccount(
        employeeId: String,
        oldMainAccId: String?,
        newMainAccId: String,
        type: EmployeeType
    ) {
        val collection = when (type) {
            EmployeeType.DRIVER -> collectionDriver
            EmployeeType.ADMIN -> collectionAdmin
        }

        oldMainAccId?.let {
            collection
                .document(employeeId)
                .collection(FIRESTORE_COLLECTION_BANK)
                .document(it)
                .update("mainAccount", false)
                .await()
        }

        collection
            .document(employeeId)
            .collection(FIRESTORE_COLLECTION_BANK)
            .document(newMainAccId)
            .update("mainAccount", true)
            .await()

    }

    //---------------------------------------------------------------------------------------------//
    // HELPERS
    //---------------------------------------------------------------------------------------------//

    private fun DocumentSnapshot.toEmployeeObject(): Employee? {
        return this.toObject(DriverEmployeeDto::class.java)?.toModel()
    }

    private fun QuerySnapshot.toEmployeeList(): List<Employee> {
        return this.mapNotNull { document ->
            document.toEmployeeObject()
        }
    }

    private fun QuerySnapshot.toBankList(): List<BankAccount> {
        return this.mapNotNull { document ->
            document.toBankObject()
        }
    }

    private fun DocumentSnapshot.toBankObject(): BankAccount? {
        return this.toObject(BankAccountDto::class.java)?.toModel()
    }

    private fun getCollectionReference(type: EmployeeType): CollectionReference {
        val collection = when (type) {
            EmployeeType.DRIVER -> collectionDriver
            EmployeeType.ADMIN -> collectionAdmin
        }
        return collection
    }

    private inline fun <reified T : EmployeeDto> querySnapShotMapper(
        querySnapShot: QuerySnapshot
    ): Resource<List<Employee>> {
        return try {
            val dataSet = querySnapShot.documents.mapNotNull { document ->
                document.toObject<T>()?.let { driver ->
                    driver.toModel()
                }
            }
            Resource(data = dataSet)
        } catch (e: Exception) {
            val message = when (e) {
                is InvalidParameterException -> "Invalid type for Employee"
                is NullPointerException -> "Null dto object for Employee"
                else -> "Erro desconhecido"
            }
            Resource(data = emptyList(), error = message)
        }
    }


}
