package br.com.apps.usecase

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Document
import br.com.apps.repository.Response
import br.com.apps.repository.repository.DocumentRepository

class DocumentUseCase(private val repository: DocumentRepository) {

    fun getAll(uid: String): LiveData<List<Document>> {
        return repository.getAll(uid)
    }

    fun addNewDocument() {

    }

    /**
     * Retrieves a document by its Id.
     *
     * @param id The document Id.
     *
     * @return LiveData containing the response of the request.
     */
    fun getById(id: String): LiveData<Response<Document>> {
        return repository.getById(id)
    }

    /**
     * Retrieves a list of documents filtered by truckId.
     *
     * @param truckId The truck Id.
     *
     * @return LiveData containing the response of the request.
     */
    suspend fun getByTruckId(truckId: String): LiveData<Response<List<Document>>> {
        return repository.getByTruckId(truckId)
    }

}