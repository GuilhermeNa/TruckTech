package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.mapper.DocumentMapper
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.Document
import br.com.apps.repository.Response
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val FIRESTORE_COLLECTION_DOCUMENTS = "documents"
private const val MASTER_USER_ID = "uid"
private const val TRUCK_ID = "truckId"

class DocumentRepository(private val fireBase: FirebaseFirestore) {

    private val collection = fireBase.collection(FIRESTORE_COLLECTION_DOCUMENTS)

    /**
     * Retrieve a list of documents for the currently logged-in master user.
     *
     * @param masterUid The master user Id.
     *
     * @return The created list of documents.
     */
    fun getAll(masterUid: String): LiveData<List<Document>> {
        val liveData = MutableLiveData<List<Document>>()
        collection.whereEqualTo(MASTER_USER_ID, masterUid)
        collection.addSnapshotListener { s, _ ->
            s?.let { snapShot ->
                liveData.value = getMappedDataSet(snapShot)
            }
        }
        return liveData
    }

    private fun getMappedDataSet(snapShot: QuerySnapshot): List<Document> {
        return snapShot.documents.mapNotNull {
            it.toObject<DocumentDto>()?.let { documentDto ->
                DocumentMapper.toModel(documentDto)
            }
        }
    }

    /**
     * Retrieve a document by its Id.
     *
     * @param id The document Id.
     *
     * @return LiveData containing the response.
     */
    fun getById(id: String): LiveData<Response<Document>> {
        val liveData = MutableLiveData<Response<Document>>()
        collection.document(id).addSnapshotListener { s, _ ->
            s?.let { document ->
                document.toObject<DocumentDto>()?.let { documentDto ->
                    DocumentMapper.toModel(documentDto)
                }?.let {
                    liveData.postValue(Response.Success(data = it))
                }
            }
        }
        return liveData
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

}


