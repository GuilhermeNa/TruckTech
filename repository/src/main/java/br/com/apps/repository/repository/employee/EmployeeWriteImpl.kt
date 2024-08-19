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

}