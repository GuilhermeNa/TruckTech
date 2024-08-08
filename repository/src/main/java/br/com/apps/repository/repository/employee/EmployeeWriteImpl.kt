package br.com.apps.repository.repository.employee

import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EMPLOYEE
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmployeeWriteImpl(fireStore: FirebaseFirestore) : EmployeeWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EMPLOYEE)

    override suspend fun save(dto: EmployeeDto): String = withContext(Dispatchers.IO) {
        return@withContext when (dto.id) {
            null -> create(dto)
            else -> update(dto)
        }
    }

    private fun create(dto: EmployeeDto): String {
        val doc = collection.document()
        dto.id = doc.id
        doc.set(dto)
        return doc.id
    }

    private fun update(dto: EmployeeDto): String {
        dto.id!!.validateId()?.let { throw EmptyIdException(EMPTY_ID) }
        val doc = collection.document(dto.id!!)
        doc.set(dto)
        return dto.id!!
    }

    override suspend fun delete(id: String) {
        withContext(Dispatchers.IO) {
            id.validateId()?.let { throw EmptyIdException(EMPTY_ID) }
            collection.document(id).delete()
        }
    }

    /*
        override suspend fun save(dto: EmployeeDto): String {
            val collection = when (dto) {
                is DriverDto -> getCollectionReference(WorkRole.DRIVER)
                is AssistantDto -> getCollectionReference(WorkRole.ASSISTANT)
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

        override suspend fun delete(id: String, type: WorkRole) {
            val collection = getCollectionReference(type)
            collection.document(id).delete().await()
        }

        override suspend fun deleteBankAcc(employeeId: String, bankId: String, type: WorkRole) {
            val collection = getCollectionReference(type)
            collection.document(employeeId).collection(FIRESTORE_COLLECTION_BANK).document(bankId).delete()
        }

        override suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: WorkRole) {
            val collection = getCollectionReference(type)

            if (bankAccDto.id == null) {
                createBankAcc(bankAccDto, collection)
            } else {
                updateBankAcc(bankAccDto, collection)
            }

        }

        private fun createBankAcc(dto: BankAccountDto, collection: CollectionReference) {
            val document = collection.document(dto.employeeId!!)
                .collection(FIRESTORE_COLLECTION_BANK).document()
            dto.id = document.id
            document.set(dto)
        }

        private fun updateBankAcc(dto: BankAccountDto, collection: CollectionReference) {
            val document = collection.document(dto.employeeId!!)
                    .collection(FIRESTORE_COLLECTION_BANK).document(dto.id!!)
            document.set(dto)
        }

        override suspend fun updateMainAccount(
            employeeId: String,
            oldMainAccId: String?,
            newMainAccId: String,
            type: WorkRole
        ) {
            val collection = getCollectionReference(type)

            oldMainAccId?.let {
                collection.document(employeeId).collection(FIRESTORE_COLLECTION_BANK)
                    .document(it).update(MAIN_ACCOUNT, false)
            }

            collection.document(employeeId).collection(FIRESTORE_COLLECTION_BANK)
                .document(newMainAccId).update(MAIN_ACCOUNT, true)

        }

        private fun getCollectionReference(type: WorkRole): CollectionReference {
            val collection = when (type) {
                WorkRole.DRIVER -> collectionDriver
                WorkRole.ASSISTANT -> collectionAdmin
            }
            return collection
        }*/

}