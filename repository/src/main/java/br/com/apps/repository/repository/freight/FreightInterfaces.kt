package br.com.apps.repository.repository.freight

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response

interface FreightRepositoryInterface : FreightWriteInterface, FreightReadInterface

interface FreightWriteInterface {

    /**
     * Saves the [FreightDto] object.
     * - If the ID of the [FreightDto] is null, it creates a new [Freight].
     * - If the ID is not null, it updates the existing [Freight].
     *
     * @param dto The [FreightDto] object to be saved.
     */
    suspend fun save(dto: FreightDto)

    /**
     * Deletes an [Freight] document from the database based on the specified ID.
     *
     * @param freightId The ID of the document to be deleted.
     */
    suspend fun delete(freightId: String?)

}

interface FreightReadInterface {

    /**
     * Fetches the [Freight] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Freight>>>

    /**
     * Fetches the [Freight] dataSet for the specified driver ID list.
     *
     * @param driverIdList The ID list of the [Employee]'s.
     * @param isPaid The payment status.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByDriverIdsAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>>

    /**
     * Fetches the [Freight] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @param isPaid The payment status.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>>

    /**
     * Fetches the [Freight] dataSet for the specified travel ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByTravelId(travelId: String, flow: Boolean = false)
            : LiveData<Response<List<Freight>>>

    /**
     * Fetches the [Freight] dataSet for the specified travel ID list.
     *
     * @param travelIdList The ID list of the [Travel]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByTravelIds(travelIdList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Freight>>>

    /**
     * Fetches the [Freight] dataSet for the specified ID.
     *
     * @param freightId The ID of the [Freight].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightById(freightId: String, flow: Boolean = false)
            : LiveData<Response<Freight>>

    /**
     * Retrieves a list of [Freight] associated with a driver ID that have not been paid yet.
     *
     * @param driverId The ID of the driver to retrieve freights for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Freight objects or an Error with an exception.
     */
    suspend fun getFreightListByDriverIdAndIsNotPaidYet(driverId: String, flow: Boolean = false)
    : LiveData<Response<List<Freight>>>

}
