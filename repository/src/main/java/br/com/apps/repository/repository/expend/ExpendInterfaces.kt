package br.com.apps.repository.repository.expend

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response

interface ExpendRepositoryInterface : ExpendReadInterface, ExpendWriteInterface

interface ExpendReadInterface {

    /**
     * Fetches the [Expend] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun fetchExpendListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Expend>>>

    /**
     * Fetches the [Expend] dataSet for the specified driver ID list.
     *
     * @param driverIdList The ID list of the [Employee]'s.
     * @param paidByEmployee If the [Expend] was paid by the employee.
     * @param alreadyRefunded If the [Expend] has already been refunded.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun fetchExpendListByDriverIdsAndRefundableStatus(
        driverIdList: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>>

    /**
     * Fetches the [Expend] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param paidByEmployee If the [Expend] was paid by the employee.
     * @param alreadyRefunded If the [Expend] has already been refunded.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun fetchExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>>

    /**
     * Fetches the [Expend] dataSet for the specified travel ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun fetchExpendListByTravelId(travelId: String, flow: Boolean = false)
            : LiveData<Response<List<Expend>>>

    /**
     * Fetches the [Expend] dataSet for the specified driver ID list.
     *
     * @param driverIdList The ID list of the [Employee]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun fetchExpendListByTravelIds(travelIdList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Expend>>>

    /**
     * Fetches the [Expend] dataSet for the specified ID.
     *
     * @param expendId The ID list of the [Expend].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun fetchExpendById(expendId: String, flow: Boolean = false)
            : LiveData<Response<Expend>>

    /**
     * Retrieves a list of [Expend] associated with a driver ID that have not been refunded yet.
     *
     * @param driverId The ID of the driver to retrieve expenditures for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Expend objects or an Error with an exception.
     */
    suspend fun fetchExpendListByDriverIdAndIsNotRefundYet(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Expend>>>

}

interface ExpendWriteInterface {

    /**
     * Deletes an [Expend] record based on the specified expenditure ID.
     *
     * @param expendId The ID of the expenditure to delete.
     */
    suspend fun delete(expendId: String?)

    /**
     * Saves [Expend] data provided in the DTO (Data Transfer Object).
     *
     * @param dto The DTO containing the expenditure data to be saved.
     */
    suspend fun save(dto: ExpendDto)

}