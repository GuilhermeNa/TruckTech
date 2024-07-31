package br.com.apps.repository.repository.travel

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response


interface TravelRepositoryInterface : TravelReadInterface, TravelWriteInterface

interface TravelWriteInterface {

    /**
     * Deletes a travel record based on the specified travel ID.
     *
     * @param travelId The ID of the travel record to delete.
     */
    suspend fun delete(travelId: String)

    /**
     * Saves travel data provided in the DTO (Data Transfer Object).
     *
     * @param dto The DTO containing the travel data to be saved.
     */
    suspend fun save(dto: TravelDto)

}

interface TravelReadInterface {

    /**
     * Retrieves a list of [Travel]'s records associated with a driver ID and their finished status.
     *
     * @param driverId The ID of the driver to retrieve travel records for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Travel objects or an Error with an exception.
     */
    suspend fun fetchTravelListByDriverIdAndIsFinished(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Travel>>>

    /**
     * Fetches the [Travel] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel] list.
     */
    suspend fun fetchTravelListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Travel>>>

    /**
     * Fetches the [Travel] dataSet for the specified ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel].
     */
    suspend fun fetchTravelById(travelId: String, flow: Boolean = false)
            : LiveData<Response<Travel>>

}

