package br.com.apps.repository.repository.travel

import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class TravelWrite(fireStore: FirebaseFirestore): TravelWriteI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    override suspend fun delete(travelId: String) {
        collection
            .document(travelId)
            .delete()
            .await()
    }

    override suspend fun save(dto: TravelDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun update(dto: TravelDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto).await()
    }

    private suspend fun create(dto: TravelDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

}