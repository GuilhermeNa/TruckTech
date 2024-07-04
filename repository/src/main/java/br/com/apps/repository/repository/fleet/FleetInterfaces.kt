package br.com.apps.repository.repository.fleet

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.TruckDto
import br.com.apps.model.model.Truck
import br.com.apps.model.model.employee.Employee
import br.com.apps.repository.util.Response

interface FleetRepositoryInterface : FleetReadInterface, FleetWriteInterface

interface FleetReadInterface {

    /**
     * Fetches the [Truck] dataSet for the specified master UID.
     *
     * @param masterUid The ID of the master user.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck] list.
     */
    suspend fun getTruckListByMasterUid(masterUid: String, flow: Boolean = false)
            : LiveData<Response<List<Truck>>>

    /**
     * Fetches the [Truck] dataSet for the specified ID.
     *
     * @param truckId The ID of [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck].
     */
    suspend fun getTruckById(truckId: String, flow: Boolean = false)
            : LiveData<Response<Truck>>

    /**
     * Fetches the [Truck] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck] list.
     */
    suspend fun getTruckByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<Truck>>

}

interface FleetWriteInterface {

    /**
     * Deletes a truck record based on the specified truck ID.
     *
     * @param truckId The ID of the truck to delete.
     */
    suspend fun delete(truckId: String)

    /**
     * Saves truck data provided in the DTO (Data Transfer Object).
     *
     * @param dto The DTO containing the truck data to be saved.
     */
    suspend fun save(dto: TruckDto)

}