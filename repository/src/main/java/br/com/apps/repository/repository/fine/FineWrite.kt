package br.com.apps.repository.repository.fine

import br.com.apps.model.dto.FineDto
import br.com.apps.model.model.Fine
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FINES
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class FineWrite(fireStore: FirebaseFirestore): FineWriteI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FINES)

    /**
     * Deletes an [Fine] document from the database based on the specified ID.
     *
     * @param fineId The ID of the document to be deleted.
     */
    override suspend fun delete(fineId: String) {
        collection.document(fineId).delete().await()
    }

    /**
     * Saves the [Fine] object.
     * If the ID of the [FineDto] is null, it creates a new [Fine].
     * If the ID is not null, it updates the existing [Fine].
     *
     * @param dto The [FineDto] object to be saved.
     */
    override suspend fun save(dto: FineDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: FineDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

    private suspend fun update(dto: FineDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto).await()
    }

}