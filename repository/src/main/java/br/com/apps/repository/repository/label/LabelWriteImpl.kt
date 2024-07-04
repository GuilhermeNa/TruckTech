package br.com.apps.repository.repository.label

import br.com.apps.model.dto.LabelDto
import br.com.apps.repository.util.FIRESTORE_COLLECTION_USER_LABELS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LabelWriteImpl(fireStore: FirebaseFirestore): LabelWriteInterface {

    private val userCollection = fireStore.collection(FIRESTORE_COLLECTION_USER_LABELS)

    override suspend fun delete(id: String) {
        userCollection.document(id).delete().await()
    }

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