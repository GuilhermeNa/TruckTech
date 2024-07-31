package br.com.apps.repository.repository.fine

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.FleetFineDto
import br.com.apps.model.model.FleetFine
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.fleet.Truck
import br.com.apps.repository.util.Response

interface FineRepositoryInterface : FineWriteInterface, FineReadInterface

interface FineWriteInterface {

    /**
     * Deletes an [FleetFine] document from the database based on the specified ID.
     *
     * @param fineId The ID of the document to be deleted.
     */
    suspend fun delete(fineId: String)

    /**
     * Saves the [FleetFine] object.
     * If the ID of the [FleetFineDto] is null, it creates a new [FleetFine].
     * If the ID is not null, it updates the existing [FleetFine].
     *
     * @param dto The [FleetFineDto] object to be saved.
     */
    suspend fun save(dto: FleetFineDto)

}

interface FineReadInterface {

    /**
     * Fetches the [FleetFine] dataSet for the specified driver ID.
     *
     * @param id The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [FleetFine] list.
     */
    suspend fun fetchFineListByDriverId(id: String, flow: Boolean = false)
            : LiveData<Response<List<FleetFine>>>

    /**
     * Fetches the [FleetFine] dataSet for the specified truck ID.
     *
     * @param id The ID of the [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [FleetFine] list.
     */
    suspend fun fetchFineListByFleetId(id: String, flow: Boolean = false)
            : LiveData<Response<List<FleetFine>>>

    /**
     * Fetches the [FleetFine] dataSet for the specified ID.
     *
     * @param id The ID of the [FleetFine].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [FleetFine] list.
     */
    suspend fun fetchFineById(id: String, flow: Boolean = false)
            : LiveData<Response<FleetFine>>

}