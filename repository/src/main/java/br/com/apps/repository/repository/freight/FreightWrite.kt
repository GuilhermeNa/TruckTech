package br.com.apps.repository.repository.freight

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FREIGHTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class FreightWrite(fireStore: FirebaseFirestore): FreightWriteI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    /**
     * Saves the [FreightDto] object.
     * - If the ID of the [FreightDto] is null, it creates a new [Freight].
     * - If the ID is not null, it updates the existing [Freight].
     *
     * @param dto The [FreightDto] object to be saved.
     */
    override suspend fun save(dto: FreightDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: FreightDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

    private suspend fun update(dto: FreightDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()
    }

    /**
     * Deletes an [Freight] document from the database based on the specified ID.
     *
     * @param freightId The ID of the document to be deleted.
     */
    override suspend fun delete(freightId: String) {
        collection
            .document(freightId)
            .delete()
            .await()
    }

}