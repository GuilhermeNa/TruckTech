package br.com.apps.repository.repository.outlay

import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EXPENDS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class OutlayWriteImpl(fireStore: FirebaseFirestore) : OutlayWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)

    override suspend fun delete(expendId: String?) {
        withContext(Dispatchers.IO) {
            if (expendId == null) throw NullPointerException("Id is null")
            collection.document(expendId).delete()
        }
    }

    override suspend fun save(dto: OutlayDto) {
        withContext(Dispatchers.IO) {
            if (dto.id == null) {
                create(dto)
            } else {
                update(dto)
            }
        }
    }

    private fun create(dto: OutlayDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

    private fun update(dto: OutlayDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
    }

}