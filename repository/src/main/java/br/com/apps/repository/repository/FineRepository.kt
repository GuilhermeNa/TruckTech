package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.FineDto
import br.com.apps.model.model.Fine
import br.com.apps.model.model.Truck
import br.com.apps.model.model.employee.Employee
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FINES
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRUCK_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toFineList
import br.com.apps.repository.util.toFineObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class FineRepository(fireStore: FirebaseFirestore) {

    private val write = FinWrite(fireStore)
    private val read = FinRead(fireStore)

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun delete(fineId: String) = write.delete(fineId)

    suspend fun save(dto: FineDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getFineListByDriverId(driverId: String, flow: Boolean = false) =
        read.getFineListByDriverId(driverId, flow)

    suspend fun getFineListByTruckId(truckId: String, flow: Boolean = false) =
        read.getFineListByTruckId(truckId, flow)

    suspend fun getFineById(fineId: String, flow: Boolean = false) = read.getFineById(fineId, flow)

}

class FinRead(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FINES)

    /**
     * Fetches the [Fine] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Fine] list.
     */
    suspend fun getFineListByDriverId(
        driverId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Fine>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return if (flow) listener.onSnapShot { it.toFineList() }
        else listener.onComplete { it.toFineList() }
    }

    /**
     * Fetches the [Fine] dataSet for the specified truck ID.
     *
     * @param truckId The ID of the [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Fine] list.
     */
    suspend fun getFineListByTruckId(
        truckId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Fine>>> {
        val listener = collection.whereEqualTo(TRUCK_ID, truckId)

        return if (flow) listener.onSnapShot { it.toFineList() }
        else listener.onComplete { it.toFineList() }
    }

    /**
     * Fetches the [Fine] dataSet for the specified ID.
     *
     * @param fineId The ID of the [Fine].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Fine] list.
     */
    suspend fun getFineById(fineId: String, flow: Boolean = false): LiveData<Response<Fine>> {
        val listener = collection.document(fineId)

        return if (flow) listener.onSnapShot { it.toFineObject() }
        else listener.onComplete { it.toFineObject() }
    }

}

class FinWrite(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FINES)

    /**
     * Deletes an [Fine] document from the database based on the specified ID.
     *
     * @param fineId The ID of the document to be deleted.
     */
    suspend fun delete(fineId: String) {
        collection.document(fineId).delete().await()
    }

    /**
     * Saves the [Fine] object.
     * If the ID of the [FineDto] is null, it creates a new [Fine].
     * If the ID is not null, it updates the existing [Fine].
     *
     * @param dto The [FineDto] object to be saved.
     */
    suspend fun save(dto: FineDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: FineDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

    private suspend fun update(dto: FineDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto).await()
    }

}
