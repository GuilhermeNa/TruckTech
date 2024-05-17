package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.Document
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.toDocumentList
import br.com.apps.repository.util.toDocumentObject
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val FIRESTORE_COLLECTION_DOCUMENTS = "documents"
private const val MASTER_USER_ID = "uid"
private const val TRUCK_ID = "truckId"

class DocumentRepository(private val fireBase: FirebaseFirestore) {

    private val collection = fireBase.collection(FIRESTORE_COLLECTION_DOCUMENTS)

    /**
     * Retrieve a document by its Id.
     *
     * @param id The document Id.
     *
     * @return LiveData containing the response.
     */
    suspend fun getDocumentById(id: String): LiveData<Response<Document>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<Document>>()

            collection.document(id).get().addOnCompleteListener { task ->
                task.exception?.let { e ->
                    liveData.postValue(Response.Error(e))
                }
                task.result?.let { document ->
                    liveData.postValue(Response.Success(document.toDocumentObject()))
                }
            }.await()

            return@withContext liveData
        }
    }

    /**
     * Save a document to Firestore.
     *
     * @param documentDto The DocumentDto object to be saved.
     *
     * @return The ID of the saved document.
     */
    suspend fun save(documentDto: DocumentDto): String {
        val document = collection.document()
        document.set(documentDto).await()
        return document.id
    }

    /**
     * Edit an existing document in Firestore.
     *
     * @param documentDto The updated DocumentDto object.
     */
    suspend fun edit(documentDto: DocumentDto) {
        val dtoId = documentDto.id ?: throw IllegalArgumentException("Id cannot be null")
        val document = collection.document(dtoId)
        document.set(documentDto).await()
    }

    /**
     * Retrieve a list of documents filtered by truckId.
     *
     * @param truckId The truck Id.
     *
     * @return LiveData containing the response of the request.
     */
    suspend fun getByTruckId(truckId: String): LiveData<Response<List<Document>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Document>>>()

            collection.whereEqualTo(TRUCK_ID, truckId)
                .addSnapshotListener { query, error ->
                    error?.let {
                        liveData.postValue(Response.Error(error))
                        return@addSnapshotListener
                    }
                    val dataSet = query?.toObjectList()
                    liveData.postValue(Response.Success(data = dataSet))
                }

            return@withContext liveData
        }
    }

    private fun QuerySnapshot.toObjectList(): List<Document> {
        return this.mapNotNull { document ->
            document.toModelObject()
        }
    }

    private fun DocumentSnapshot.toModelObject(): Document? {
        return this.toObject(DocumentDto::class.java)?.toModel()
    }

    /**
     * Retrieve a list of documents filtered by truckId.
     *
     * @param truckId The truck Id.
     * @param withFlow If the user wants to keep observing the source or not.
     *
     * @return LiveData containing a [Response] object with a list of [Document].
     */
    suspend fun getDocumentListByTruckId(
        truckId: String,
        withFlow: Boolean = false
    ): LiveData<Response<List<Document>>> {
        return withContext(Dispatchers.IO) {
            val liveData = MutableLiveData<Response<List<Document>>>()
            val listener = collection.whereEqualTo(TRUCK_ID, truckId)

            if (withFlow) {
                listener.addSnapshotListener { nQuery, error ->
                    error?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    nQuery?.let { query ->
                        liveData.postValue(Response.Success(data = query.toDocumentList()))
                    }
                }
            } else {
                listener.get().addOnCompleteListener { task ->
                    task.exception?.let { e ->
                        liveData.postValue(Response.Error(e))
                    }
                    task.result?.let { query ->
                        liveData.postValue(Response.Success(data = query.toDocumentList()))
                    }
                }
            }

            return@withContext liveData
        }
    }

}


