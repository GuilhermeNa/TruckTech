package br.com.apps.repository.repository.refuel

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response

interface RefuelRepositoryInterface : RefuelWriteInterface, RefuelReadInterface

interface RefuelWriteInterface {

    /**
     * Saves the [RefuelDto] object.
     * If the ID of the [RefuelDto] is null, it creates a new [Refuel].
     * If the ID is not null, it updates the existing [Refuel].
     *
     * @param dto The [RefuelDto] object to be saved.
     */
    suspend fun save(dto: RefuelDto)

    /**
     * Deletes an [Refuel] document from the database based on the specified ID.
     *
     * @param refuelId The ID of the document to be deleted.
     */
    suspend fun delete(refuelId: String?)

}

interface RefuelReadInterface {

    /**
     * Fetches the [Refuel] dataSet for the specified driver ID.
     *
     * @param id The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel] list.
     */
    suspend fun fetchRefuelListByDriverId(id: String, flow: Boolean = false)
            : LiveData<Response<List<Refuel>>>

    /**
     * Fetches the [Refuel] dataSet for the specified travel ID.
     *
     * @param id The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel] list.
     */
    suspend fun fetchRefuelListByTravelId(id: String, flow: Boolean = false)
            : LiveData<Response<List<Refuel>>>

    /**
     * Fetches the [Refuel] dataSet for the specified travel IDs.
     *
     * @param ids The ID of the [Travel]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel] list.
     */
    suspend fun fetchRefuelListByTravelIds(ids: List<String>, flow: Boolean = false)
            : LiveData<Response<List<Refuel>>>

    /**
     * Fetches the [Refuel] dataSet for the specified ID.
     *
     * @param id The ID of the [Refuel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel].
     */
    suspend fun fetchRefuelById(id: String, flow: Boolean = false)
            : LiveData<Response<Refuel>>

}
