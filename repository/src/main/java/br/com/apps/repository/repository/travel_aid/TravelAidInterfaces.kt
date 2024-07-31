package br.com.apps.repository.repository.travel_aid

import androidx.lifecycle.LiveData
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.util.Response

interface TravelAidInterface : TravelAidReadInterface

interface TravelAidReadInterface {

    /**
     * Retrieves a [TravelAid]'s records associated with a driver ID that have not been discounted yet.
     *
     * @param employeeId The ID of the driver to retrieve cost help records for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of TravelAid objects or an Error with an exception.
     */
    suspend fun fetchTravelAidByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean = false
    ): LiveData<Response<List<TravelAid>>>

    /**
     * Retrieves a list of [TravelAid]'s records associated with a specific travel ID.
     *
     * @param travelId The ID of the travel to retrieve travel aid records for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of TravelAid objects or an Error with an exception.
     */
    suspend fun fetchTravelAidListByTravelId(travelId: String, flow: Boolean = false)
            : LiveData<Response<List<TravelAid>>>

    /**
     * Retrieves a list of [TravelAid]'s records associated with multiple travel IDs.
     *
     * @param travelIdList The list of travel IDs to retrieve travel aid records for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of TravelAid objects or an Error with an exception.
     */
    suspend fun fetchTravelAidListByTravelIds(travelIdList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<TravelAid>>>

    /**
     * Retrieves a list of [TravelAid]'s records associated with a specific driver ID.
     *
     * @param driverId The ID of the driver to retrieve travel aid records for.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of TravelAid objects or an Error with an exception.
     */
    suspend fun fetchTravelAidListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<TravelAid>>>

}