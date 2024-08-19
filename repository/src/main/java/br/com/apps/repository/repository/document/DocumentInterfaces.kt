package br.com.apps.repository.repository.document

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.document.TruckDocumentDto
import br.com.apps.model.model.document.TruckDocument
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
     * @param id The ID of the [TruckDocument].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [TruckDocument].
     */
    suspend fun fetchDocumentById(id: String, flow: Boolean = false)
            : LiveData<Response<TruckDocument>>

    /**
     * Fetches the [TruckDocument] dataSet for the specified truck ID.
     *
     * @param id The ID of the [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [TruckDocument] list.
     */
    suspend fun fetchDocumentListByFleetId(id: String, flow: Boolean = false)
            : LiveData<Response<List<TruckDocument>>>

    /**
     * Fetches the list of [TruckDocument] datasets for a list of truck IDs.
     *
     * @param ids A list of IDs for the [Truck]'s.
     * @param flow If true, the user will continuously observe updates to the data.
     * @return A [Response] with a list of [TruckDocument] for the provided truck IDs.
     * @throws EmptyDataException When [ids] is empty.
     */
    suspend fun fetchDocumentListByFleetIdList(ids: List<String>, flow: Boolean = false)
            : LiveData<Response<List<TruckDocument>>>

}