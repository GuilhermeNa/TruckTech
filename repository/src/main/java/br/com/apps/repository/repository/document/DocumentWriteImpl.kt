package br.com.apps.repository.repository.document

import br.com.apps.model.dto.TruckDocumentDto
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DOCUMENTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DocumentWriteImpl(fireStore: FirebaseFirestore): DocumentWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_DOCUMENTS)

    override suspend fun save(dto: TruckDocumentDto) {
        if (dto.id == null) create(dto)
        else update(dto)
    }

    private suspend fun update(dto: TruckDocumentDto) {
        val document = collection.document(dto.id!!)
        document.set(dto).await()
    }

    private suspend fun create(dto: TruckDocumentDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

}