package br.com.apps.repository.repository.travel

import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class TravelWriteImpl(fireStore: FirebaseFirestore) : TravelWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    override suspend fun delete(travelId: String) {
        collection.document(travelId).delete()
    }

    override suspend fun save(dto: TravelDto) {
        withContext(Dispatchers.IO) {
            if (dto.id == null) create(dto)
            else update(dto)
        }
    }

    private fun update(dto: TravelDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
    }

    private fun create(dto: TravelDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

}