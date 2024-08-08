package br.com.apps.repository.repository.bank_account

import br.com.apps.model.dto.bank.BankAccountDto
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EMPLOYEE_BANK_ACCOUNTS
import br.com.apps.repository.util.MAIN_ACCOUNT
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BankAccountWriteImpl(firestore: FirebaseFirestore) : BankAccountWriteInterface {

    private val collection = firestore.collection(FIRESTORE_COLLECTION_EMPLOYEE_BANK_ACCOUNTS)

    override suspend fun save(dto: BankAccountDto) {
        withContext(Dispatchers.IO) {
            when (dto.id) {
                null -> create(dto)
                else -> update(dto)
            }
        }
    }

    private fun create(dto: BankAccountDto): String {
        val doc = collection.document()
        dto.id = doc.id
        doc.set(dto)
        return doc.id
    }

    private fun update(dto: BankAccountDto): String {
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

    override suspend fun updateMainAccount(oldMainAccId: String?, newMainAccId: String) {
        withContext(Dispatchers.IO) {
            oldMainAccId?.let { collection.document(it).update(MAIN_ACCOUNT, false) }
            collection.document(newMainAccId).update(MAIN_ACCOUNT, true)
        }
    }

}