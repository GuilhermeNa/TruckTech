package br.com.apps.repository.repository.document

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.TruckDocumentDto
import br.com.apps.model.model.TruckDocument
import br.com.apps.model.model.fleet.Truck
import br.com.apps.repository.util.Response

interface DocumentRepositoryInterface : DocumentWriteInterface, DocumentReadInterface

interface DocumentWriteInterface {

    /**
     * Saves document data represented by a DTO (Data Transfer Object).
     * @param dto The DTO (Data Transfer Object) containing the data to be saved.
     */
    suspend fun save(dto: TruckDocumentDto)

}

interface DocumentReadInterface {

    /**
     * Fetches the [TruckDocument] dataSet for the specified ID.
     *
     * @param documentId The ID of the [TruckDocument].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [TruckDocument].
     */
    suspend fun getDocumentById(documentId: String, flow: Boolean = false)
            : LiveData<Response<TruckDocument>>

    /**
     * Fetches the [TruckDocument] dataSet for the specified truck ID.
     *
     * @param truckId The ID of the [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [TruckDocument] list.
     */
    suspend fun getDocumentListByTruckId(truckId: String, flow: Boolean = false)
            : LiveData<Response<List<TruckDocument>>>

    suspend fun fetchDocumentListByTruckIdList(idList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<TruckDocument>>>


}