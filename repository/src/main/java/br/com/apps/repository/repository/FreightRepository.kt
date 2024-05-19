package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FREIGHTS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toFreightList
import br.com.apps.repository.util.toFreightObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

private const val IS_COMMISSION_PAID = "isCommissionPaid"

class FreightRepository(fireStore: FirebaseFirestore) {

    private val read = FreRead(fireStore)
    private val write = FreWrite(fireStore)

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun save(dto: FreightDto) = write.save(dto)

    suspend fun delete(freightId: String) = write.delete(freightId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getFreightListByDriverId(driverId: String, flow: Boolean = false) =
        read.getFreightListByDriverId(driverId, flow)

    suspend fun getFreightListByTravelId(travelId: String, flow: Boolean = false) =
        read.getFreightListByTravelId(travelId, flow)

    suspend fun getFreightListByTravelIds(idList: List<String>, flow: Boolean = false) =
        read.getFreightListByTravelIds(idList, flow)

    suspend fun getFreightById(freightId: String, flow: Boolean = false) =
        read.getFreightById(freightId, flow)

    suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean = false
    ) =
        read.getFreightListByDriverIdsAndPaymentStatus(driverIdList, isPaid, flow)

    suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        flow: Boolean = false
    ) =
        read.getFreightListByDriverIdAndPaymentStatus(driverId, isPaid, flow)

}

private class FreWrite(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    /**
     * Saves the [FreightDto] object.
     * - If the ID of the [FreightDto] is null, it creates a new [Freight].
     * - If the ID is not null, it updates the existing [Freight].
     *
     * @param dto The [FreightDto] object to be saved.
     */
    suspend fun save(dto: FreightDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: FreightDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto).await()
        return document.id
    }

    private suspend fun update(dto: FreightDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()
    }

    /**
     * Deletes an [Freight] document from the database based on the specified ID.
     *
     * @param freightId The ID of the document to be deleted.
     */
    suspend fun delete(freightId: String) {
        collection
            .document(freightId)
            .delete()
            .await()
    }

}

private class FreRead(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    /**
     * Fetches the [Freight] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByDriverId(
        driverId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    /**
     * Fetches the [Freight] dataSet for the specified driver ID list.
     *
     * @param driverIdList The ID list of the [Employee]'s.
     * @param isPaid The payment status.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByDriverIdsAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>> {
        val listener = collection
            .whereIn(DRIVER_ID, driverIdList)
            .whereEqualTo(IS_COMMISSION_PAID, isPaid)

        return if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    /**
     * Fetches the [Freight] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @param isPaid The payment status.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>> {
        val listener = collection
            .whereEqualTo(DRIVER_ID, driverId)
            .whereEqualTo(IS_COMMISSION_PAID, isPaid)

        return if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    /**
     * Fetches the [Freight] dataSet for the specified travel ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByTravelId(
        travelId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>> {
        val listener = collection.whereEqualTo(TRAVEL_ID, travelId)

        return if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    /**
     * Fetches the [Freight] dataSet for the specified travel ID list.
     *
     * @param travelIdList The ID list of the [Travel]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightListByTravelIds(
        travelIdList: List<String>,
        flow: Boolean = false
    ): LiveData<Response<List<Freight>>> {
        val listener = collection.whereIn(TRAVEL_ID, travelIdList)

        return if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    /**
     * Fetches the [Freight] dataSet for the specified ID.
     *
     * @param freightId The ID of the [Freight].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    suspend fun getFreightById(
        freightId: String,
        flow: Boolean = false
    ): LiveData<Response<Freight>> {
        val listener = collection.document(freightId)

        return if (flow) listener.onSnapShot { it.toFreightObject() }
        else listener.onComplete { it.toFreightObject() }
    }

}