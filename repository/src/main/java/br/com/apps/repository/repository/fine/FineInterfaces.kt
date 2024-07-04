package br.com.apps.repository.repository.fine

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.FineDto
import br.com.apps.model.model.Fine
import br.com.apps.model.model.Truck
import br.com.apps.model.model.employee.Employee
import br.com.apps.repository.util.Response

interface FineRepositoryInterface : FineWriteInterface, FineReadInterface

interface FineWriteInterface {

    /**
     * Deletes an [Fine] document from the database based on the specified ID.
     *
     * @param fineId The ID of the document to be deleted.
     */
    suspend fun delete(fineId: String)

    /**
     * Saves the [Fine] object.
     * If the ID of the [FineDto] is null, it creates a new [Fine].
     * If the ID is not null, it updates the existing [Fine].
     *
     * @param dto The [FineDto] object to be saved.
     */
    suspend fun save(dto: FineDto)

}

interface FineReadInterface {

    /**
     * Fetches the [Fine] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Fine] list.
     */
    suspend fun getFineListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Fine>>>

    /**
     * Fetches the [Fine] dataSet for the specified truck ID.
     *
     * @param truckId The ID of the [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Fine] list.
     */
    suspend fun getFineListByTruckId(truckId: String, flow: Boolean = false)
            : LiveData<Response<List<Fine>>>

    /**
     * Fetches the [Fine] dataSet for the specified ID.
     *
     * @param fineId The ID of the [Fine].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Fine] list.
     */
    suspend fun getFineById(fineId: String, flow: Boolean = false)
            : LiveData<Response<Fine>>

}