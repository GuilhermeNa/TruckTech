package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.model.Document
import br.com.apps.model.model.Truck
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRUCK_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toDocumentList
import br.com.apps.repository.util.toDocumentObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val FIRESTORE_COLLECTION_DOCUMENTS = "documents"

class DocumentRepository(fireStore: FirebaseFirestore) {

    private val write = DocWrite(fireStore)
    private val read = DocRead(fireStore)

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun save(dto: DocumentDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getDocumentById(id: String, flow: Boolean = false) = read.getDocumentById(id, flow)

    suspend fun getDocumentListByTruckId(truckId: String, flow: Boolean = false) =
        read.getDocumentListByTruckId(truckId, flow)

}

private class DocRead(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_DOCUMENTS)

    /**
     * Fetches the [Document] dataSet for the specified ID.
     *
     * @param documentId The ID of the [Document].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Document].
     */
    suspend fun getDocumentById(
        documentId: String,
        flow: Boolean = false
    ): LiveData<Response<Document>> {
        val listener = collection.document(documentId)

        return if (flow) listener.onSnapShot { it.toDocumentObject() }
        else listener.onComplete { it.toDocumentObject() }
    }

    /**
     * Fetches the [Document] dataSet for the specified truck ID.
     *
     * @param truckId The ID of the [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Document] list.
     */
    suspend fun getDocumentListByTruckId(
        truckId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Document>>> {
        val listener = collection.whereEqualTo(TRUCK_ID, truckId)

        return if (flow) listener.onSnapShot { it.toDocumentList() }
        else listener.onComplete { it.toDocumentList() }
    }

}

private class DocWrite(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_DOCUMENTS)

    suspend fun save(dto: DocumentDto) {
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



