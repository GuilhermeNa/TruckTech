package br.com.apps.repository.repository.fleet

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.fleet.TruckDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.model.fleet.Truck
import br.com.apps.repository.util.Response

interface FleetRepositoryInterface : FleetReadInterface, FleetWriteInterface

interface FleetReadInterface {

    /**
     * Fetches the [Truck] dataSet for the specified master UID.
     *
     * @param uid The ID of the master user.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck] list.
     */
    suspend fun fetchTruckListByMasterUid(uid: String, flow: Boolean = false)
            : LiveData<Response<List<Truck>>>

    /**
     * Fetches the [Truck] dataSet for the specified ID.
     *
     * @param id The ID of [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck].
     */
    suspend fun fetchTruckById(id: String, flow: Boolean = false)
            : LiveData<Response<Truck>>

    /**
     * Fetches the [Truck] dataSet for the specified driver ID.
     *
     * @param id The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck] list.
     */
    suspend fun fetchTruckByDriverId(id: String, flow: Boolean = false)
            : LiveData<Response<Truck>>

    /**
     * Fetches the list of [Trailer]'s linked to the specified [Truck] ID.
     *
     * @param id The ID of the [Truck] for which the linked trailers are to be fetched.
     * @param flow If true, the user will continuously observe updates to the data.
     * @return A [Response] with a list of trailers linked to the specified truck.
     */
    suspend fun fetchTrailerListLinkedToTruckById(id: String, flow: Boolean = false)
            : LiveData<Response<List<Trailer>>>

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