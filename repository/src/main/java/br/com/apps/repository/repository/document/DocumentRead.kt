package br.com.apps.repository.repository.document

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Document
import br.com.apps.model.model.Truck
import br.com.apps.repository.util.FIRESTORE_COLLECTION_DOCUMENTS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRUCK_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toDocumentList
import br.com.apps.repository.util.toDocumentObject
import com.google.firebase.firestore.FirebaseFirestore

class DocumentRead(fireStore: FirebaseFirestore): DocumentReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_DOCUMENTS)

    /**
     * Fetches the [Document] dataSet for the specified ID.
     *
     * @param documentId The ID of the [Document].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Document].
     */
    override suspend fun getDocumentById(
        documentId: String,
        flow: Boolean
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
    override suspend fun getDocumentListByTruckId(
        truckId: String,
        flow: Boolean
    ): LiveData<Response<List<Document>>> {
        val listener = collection.whereEqualTo(TRUCK_ID, truckId)

        return if (flow) listener.onSnapShot { it.toDocumentList() }
        else listener.onComplete { it.toDocumentList() }
    }

}
