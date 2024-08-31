package br.com.apps.repository.repository.freight

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FREIGHTS
import br.com.apps.repository.util.IS_UPDATING_INVOICE
import br.com.apps.repository.util.URL_INVOICE
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class FreightWriteImpl(fireStore: FirebaseFirestore) : FreightWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    override suspend fun save(dto: FreightDto): String {
        return withContext(Dispatchers.IO) {
            return@withContext if (dto.id == null) {
                create(dto)
            } else {
                update(dto)
            }
        }
    }

    private fun create(dto: FreightDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

    private fun update(dto: FreightDto): String {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
        return id
    }

    override suspend fun delete(freightId: String?) {
        withContext(Dispatchers.IO) {
            if (freightId == null) throw NullPointerException("Id is null")
            collection.document(freightId).delete()
        }
    }

    override suspend fun updateInvoiceUrl(id: String, url: String) {
        withContext(Dispatchers.IO) {
            if (id.isEmpty()) throw NullPointerException()

            val updates = mutableMapOf<String, Any?>(
                Pair(URL_INVOICE, url),
                Pair(IS_UPDATING_INVOICE, false)
            )

            collection.document(id).update(updates)
        }
    }

}