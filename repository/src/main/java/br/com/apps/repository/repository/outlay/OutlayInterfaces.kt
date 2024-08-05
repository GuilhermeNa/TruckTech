package br.com.apps.repository.repository.outlay

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response

interface OutlayRepositoryInterface : OutlayReadInterface, OutlayWriteInterface

interface OutlayReadInterface {

    /**
     * Fetches the [Outlay] dataSet for the specified driver ID.
     *
     * @param id The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Outlay] list.
     */
    suspend fun fetchOutlayListByDriverId(id: String, flow: Boolean = false)
            : LiveData<Response<List<Outlay>>>

    /**
     * Fetches the [Outlay] dataSet for the specified driver ID list.
     *
     * @param ids The ID list of the [Employee]'s.
     * @param paidByEmployee If the [Outlay] was paid by the employee.
     * @param alreadyRefunded If the [Outlay] has already been refunded.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Outlay] list.
     */
    suspend fun fetchOutlayListByDriverIdsAndRefundableStatus(
        ids: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Outlay>>>

    /**
     * Fetches the [Outlay] dataSet for the specified driver ID.
     *
     * @param id The ID of the [Employee].
     * @param paidByEmployee If the [Outlay] was paid by the employee.
     * @param alreadyRefunded If the [Outlay] has already been refunded.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Outlay] list.
     */
    suspend fun fetchOutlayListByDriverIdAndRefundableStatus(
        id: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Outlay>>>

    /**
     * Fetches the [Outlay] dataSet for the specified travel ID.
     *
     * @param id The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Outlay] list.
     */
    suspend fun fetchOutlayListByTravelId(id: String, flow: Boolean = false)
            : LiveData<Response<List<Outlay>>>

    /**
     * Fetches the [Outlay] dataSet for the specified driver ID list.
     *
     * @param driverIdList The ID list of the [Employee]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Outlay] list.
     */
    suspend fun fetchOutlayListByTravelIds(ids: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Outlay>>>

    /**
     * Fetches the [Outlay] dataSet for the specified ID.
     *
     * @param id The ID list of the [Outlay].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Outlay] list.
     */
    suspend fun fetchOutlayById(id: String, flow: Boolean = false)
            : LiveData<Response<Outlay>>

    /**
     * Retrieves a list of [Outlay] associated with a driver ID that have not been refunded yet.
     *
     * @param id The ID of the driver to retrieve expenditures for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Outlay objects or an Error with an exception.
     */
    suspend fun fetchOutlayListByDriverIdAndIsNotRefundYet(id: String, flow: Boolean = false)
            : LiveData<Response<List<Outlay>>>

}

interface OutlayWriteInterface {

    /**
     * Deletes an [Outlay] record based on the specified expenditure ID.
     *
     * @param expendId The ID of the expenditure to delete.
     */
    suspend fun delete(expendId: String?)

    /**
     * Saves [Outlay] data provided in the DTO (Data Transfer Object).
     *
     * @param dto The DTO containing the expenditure data to be saved.
     */
    suspend fun save(dto: OutlayDto)

}