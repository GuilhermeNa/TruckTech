package br.com.apps.repository.repository.label

import br.com.apps.model.dto.LabelDto
import br.com.apps.repository.util.FIRESTORE_COLLECTION_USER_LABELS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LabelWrite(fireStore: FirebaseFirestore): LabelWriteI {

    private val userCollection = fireStore.collection(FIRESTORE_COLLECTION_USER_LABELS)

    /**
     * Delete a label.
     * @param id The id of the label.
     * @return LiveData with the result of operation.
     */
    override suspend fun delete(id: String) {
        userCollection.document(id).delete().await()
    }

    /**
     * Add a new user or edit if already exists.
     * @param dto The label sent by the user.
     * @return The ID of the saved label.
     */
    override suspend fun save(dto: LabelDto) {
        if (dto.id == null) create(dto)
        else update(dto)
    }

    private suspend fun update(dto: LabelDto) {
        val document = userCollection.document(dto.id!!)
        document.set(dto).await()
    }

    private suspend fun create(dto: LabelDto): String {
        val document = userCollection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

}