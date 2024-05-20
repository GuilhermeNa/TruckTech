package br.com.apps.repository.repository.document

import br.com.apps.model.dto.DocumentDto
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DOCUMENTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DocumentWrite(fireStore: FirebaseFirestore): DocumentWriteI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_DOCUMENTS)

    override suspend fun save(dto: DocumentDto) {
        if (dto.id == null) create(dto)
        else update(dto)
    }

    private suspend fun update(dto: DocumentDto) {
        val document = collection.document(dto.id!!)
        document.set(dto).await()
    }

    private suspend fun create(dto: DocumentDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

}