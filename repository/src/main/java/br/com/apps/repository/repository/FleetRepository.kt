package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.TruckDto
import br.com.apps.model.model.Truck
import br.com.apps.model.model.employee.Employee
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTruckList
import br.com.apps.repository.util.toTruckObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val FIRESTORE_COLLECTION_TRUCKS = "trucks"

class FleetRepository(private val fireStore: FirebaseFirestore) {

    private val write = FleWrite(fireStore)
    private val read = FleRead(fireStore)

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun save(dto: TruckDto) = write.save(dto)

    suspend fun delete(truckId: String) = write.delete(truckId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getTruckListByMasterUid(masterUid: String, flow: Boolean = false) =
        read.getTruckListByMasterUid(masterUid, flow)

    suspend fun getTruckById(truckId: String, flow: Boolean = false) =
        read.getTruckById(truckId, flow)

    suspend fun getTruckByDriverId(driverId: String, flow: Boolean = false) =
        read.getTruckByDriverId(driverId, flow)

}

private class FleWrite(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRUCKS)

    suspend fun save(dto: TruckDto) {
        if (dto.id == null) create(dto)
        else update(dto)
    }

    private suspend fun create(dto: TruckDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

    private suspend fun update(dto: TruckDto) {
        val document = collection.document(dto.id!!)
        document.set(dto).await()
    }

    suspend fun delete(truckId: String) {
        collection.document(truckId).delete().await()
    }

}

private class FleRead(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRUCKS)

    /**
     * Fetches the [Truck] dataSet for the specified master UID.
     *
     * @param masterUid The ID of the master user.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck] list.
     */
    suspend fun getTruckListByMasterUid(
        masterUid: String,
        flow: Boolean = false
    ): LiveData<Response<List<Truck>>> {
        val listener = collection.whereEqualTo(MASTER_UID, masterUid)

        return if (flow) listener.onSnapShot { it.toTruckList() }
        else listener.onComplete { it.toTruckList() }
    }

    /**
     * Fetches the [Truck] dataSet for the specified ID.
     *
     * @param truckId The ID of [Truck].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck].
     */
    suspend fun getTruckById(truckId: String, flow: Boolean = false): LiveData<Response<Truck>> {
        val listener = collection.document(truckId)

        return if (flow) listener.onSnapShot { it.toTruckObject() }
        else listener.onComplete { it.toTruckObject() }
    }

    /**
     * Fetches the [Truck] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Truck] list.
     */
    suspend fun getTruckByDriverId(driverId: String, flow: Boolean = false): LiveData<Response<Truck>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId).limit(1)

        return if (flow) listener.onSnapShot { it.toTruckList()[0] }
        else listener.onComplete { it.toTruckList()[0] }
    }

}