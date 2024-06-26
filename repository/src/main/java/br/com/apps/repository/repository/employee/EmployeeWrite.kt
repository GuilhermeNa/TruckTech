package br.com.apps.repository.repository.employee

import br.com.apps.model.dto.employee_dto.AdminEmployeeDto
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.DriverEmployeeDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ADMIN
import br.com.apps.repository.util.FIRESTORE_COLLECTION_BANK
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DRIVER
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class EmployeeWrite(fireStore: FirebaseFirestore) : EmployeeWriteI {

    private val collectionDriver = fireStore.collection(FIRESTORE_COLLECTION_DRIVER)
    private val collectionAdmin = fireStore.collection(FIRESTORE_COLLECTION_ADMIN)

    /**
     * Add or save a dto
     */
    override suspend fun save(dto: EmployeeDto): String {
        val collection = when (dto) {
            is DriverEmployeeDto -> getCollectionReference(EmployeeType.DRIVER)
            is AdminEmployeeDto -> getCollectionReference(EmployeeType.ADMIN)
            else -> throw InvalidParameterException()
        }

        return if (dto.id == null) create(dto, collection)
        else update(dto, collection)
    }

    private suspend fun create(dto: EmployeeDto, collection: CollectionReference): String {
        val doc = collection.document()
        dto.id = doc.id
        doc.set(dto).await()
        return doc.id
    }

    private suspend fun update(dto: EmployeeDto, collection: CollectionReference): String {
        val doc = collection.document(dto.id!!)
        doc.set(dto).await()
        return dto.id!!
    }

    /**
     * Delete
     */
    override suspend fun delete(id: String, type: EmployeeType) {
        val collection = getCollectionReference(type)
        collection.document(id).delete().await()
    }

    /**
     * Delete employee Bank acccount
     */
    override suspend fun deleteBankAcc(employeeId: String, bankId: String, type: EmployeeType) {
        val collection = getCollectionReference(type)

        collection.document(employeeId)
            .collection(FIRESTORE_COLLECTION_BANK)
            .document(bankId)
            .delete().await()
    }

    /**
     * Save bank account
     */
    override suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: EmployeeType) {
        val collection = getCollectionReference(type)

        if (bankAccDto.id == null) {
            createBankAcc(bankAccDto, collection)
        } else {
            updateBankAcc(bankAccDto, collection)
        }

    }

    private suspend fun createBankAcc(dto: BankAccountDto, collection: CollectionReference) {
        val document = collection.document(dto.employeeId!!)
            .collection(FIRESTORE_COLLECTION_BANK).document()

        dto.id = document.id
        document.set(dto).await()
    }

    private suspend fun updateBankAcc(dto: BankAccountDto, collection: CollectionReference) {
        val document = collection.document(dto.employeeId!!)
                .collection(FIRESTORE_COLLECTION_BANK).document(dto.id!!)

        document.set(dto).await()
    }

    /**
     * Update main acc
     */
    override suspend fun updateMainAccount(
        employeeId: String,
        oldMainAccId: String?,
        newMainAccId: String,
        type: EmployeeType
    ) {
        val collection = getCollectionReference(type)

        oldMainAccId?.let {
            collection.document(employeeId).collection(FIRESTORE_COLLECTION_BANK)
                .document(it).update("mainAccount", false).await()
        }

        collection.document(employeeId).collection(FIRESTORE_COLLECTION_BANK)
            .document(newMainAccId).update("mainAccount", true).await()

    }

    private fun getCollectionReference(type: EmployeeType): CollectionReference {
        val collection = when (type) {
            EmployeeType.DRIVER -> collectionDriver
            EmployeeType.ADMIN -> collectionAdmin
        }
        return collection
    }

}