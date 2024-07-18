package br.com.apps.repository.repository.freight

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FREIGHTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class FreightWriteImpl(fireStore: FirebaseFirestore) : FreightWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    override suspend fun save(dto: FreightDto) {
        withContext(Dispatchers.IO) {
            if (dto.id == null) {
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

    private fun update(dto: FreightDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
    }

    override suspend fun delete(freightId: String?) {
        withContext(Dispatchers.IO) {
            if (freightId == null) throw NullPointerException("Id is null")
            collection.document(freightId).delete()
        }
    }

}