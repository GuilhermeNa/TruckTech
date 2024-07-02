package br.com.apps.repository.repository.expend

import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EXPENDS
import com.google.firebase.firestore.FirebaseFirestore
import java.security.InvalidParameterException

class ExpendWrite(fireStore: FirebaseFirestore): ExpendWriteI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)

    /**
     * Deletes an [Expend] document from the database based on the specified ID.
     *
     * @param expendId The ID of the expenditure document to be deleted.
     */
    override suspend fun delete(expendId: String) {
        collection.document(expendId).delete()
    }

    /**
     * Saves the [ExpendDto] object.
     * If the ID of the [ExpendDto] is null, it creates a new [Expend].
     * If the ID is not null, it updates the existing [Expend].
     *
     * @param dto The [ExpendDto] object to be saved.
     */
    override suspend fun save(dto: ExpendDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private fun create(dto: ExpendDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

    private fun update(dto: ExpendDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
    }

}