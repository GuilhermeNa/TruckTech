package br.com.apps.repository.repository.document

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.model.Document
import br.com.apps.model.model.Truck
import br.com.apps.repository.util.Response

interface DocumentRepositoryInterface : DocumentWriteInterface, DocumentReadInterface

interface DocumentWriteInterface {

    /**
     * Saves document data represented by a DTO (Data Transfer Object).
     * @param dto The DTO (Data Transfer Object) containing the data to be saved.
     */
    suspend fun save(dto: DocumentDto)

}

interface DocumentReadInterface {

    /**
     * Fetches the [Document] dataSet for the specified ID.
     *
     * @param documentId The ID of the [Document].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Document].
     */
    suspend fun getDocumentById(documentId: String, flow: Boolean = false)
            : LiveData<Response<Document>>

    /**
     * Fetches the [Document] dataSet for the specified truck ID.
     *
     * @param truckId The ID of the [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Document] list.
     */
    suspend fun getDocumentListByTruckId(truckId: String, flow: Boolean = false)
            : LiveData<Response<List<Document>>>

}