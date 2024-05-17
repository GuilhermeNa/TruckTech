package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REFUELS
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toRefuelList
import br.com.apps.repository.util.toRefuelObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class RefuelRepository(fireStore: FirebaseFirestore) {

    private val write = RefWrite(fireStore)
    private val read = RefRead(fireStore)

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun save(dto: RefuelDto) = write.save(dto)

    suspend fun delete(refuelId: String) = write.delete(refuelId)

    suspend fun deleteRefuelForThisTravel(travelId: String, refuelId: String) =
        write.deleteRefuelForThisTravel(travelId, refuelId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getRefuelListByDriverId(driverId: String, flow: Boolean = false) =
        read.getRefuelListByDriverId(driverId, flow)

    suspend fun getRefuelListByTravelId(travelId: String, flow: Boolean = false) =
        read.getRefuelListByTravelId(travelId, flow)

    suspend fun getRefuelListByTravelIds(idList: List<String>, flow: Boolean = false) =
        read.getRefuelListByTravelIds(idList, flow)

    suspend fun getRefuelById(refuelId: String, flow: Boolean = false) =
        read.getRefuelById(refuelId, flow)

}

private class RefRead(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REFUELS)

    /**
     * Fetches the [Refuel] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel] list.
     */
    suspend fun getRefuelListByDriverId(
        driverId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Refuel>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    /**
     * Fetches the [Refuel] dataSet for the specified travel ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel] list.
     */
    suspend fun getRefuelListByTravelId(
        travelId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Refuel>>> {
        val listener = collection.whereEqualTo(TRAVEL_ID, travelId)

        return if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    /**
     * Fetches the [Refuel] dataSet for the specified travel IDs.
     *
     * @param idList The ID of the [Travel]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel] list.
     */
    suspend fun getRefuelListByTravelIds(
        idList: List<String>,
        flow: Boolean = false
    ): LiveData<Response<List<Refuel>>> {
        val listener = collection.whereIn(TRAVEL_ID, idList)

        return if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    /**
     * Fetches the [Refuel] dataSet for the specified ID.
     *
     * @param refuelId The ID of the [Refuel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel].
     */
    suspend fun getRefuelById(refuelId: String, flow: Boolean = false): LiveData<Response<Refuel>> {
        val listener = collection.document(refuelId)

        return if (flow) listener.onSnapShot { it.toRefuelObject() }
        else listener.onComplete { it.toRefuelObject() }
    }

}

private class RefWrite(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REFUELS)
    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    /**
     * Saves the [RefuelDto] object.
     * If the ID of the [RefuelDto] is null, it creates a new [Refuel].
     * If the ID is not null, it updates the existing [Refuel].
     *
     * @param dto The [RefuelDto] object to be saved.
     */
    suspend fun save(dto: RefuelDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: RefuelDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: RefuelDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()

    }

    /**
     * Deletes an [Refuel] document from the database based on the specified ID.
     *
     * @param refuelId The ID of the document to be deleted.
     */
    suspend fun delete(refuelId: String) {
        collection
            .document(refuelId)
            .delete()
            .await()
    }

    /**
     * Deletes a specific [Refuel] entry associated with a [Travel].
     *
     * @param travelId The ID of the travel from which to delete the refuel entry.
     * @param refuelId The ID of the refuel entry to delete.
     */
    suspend fun deleteRefuelForThisTravel(travelId: String, refuelId: String) {
        parentCollection
            .document(travelId)
            .collection(FIRESTORE_COLLECTION_REFUELS)
            .document(refuelId)
            .delete()
            .await()
    }

}