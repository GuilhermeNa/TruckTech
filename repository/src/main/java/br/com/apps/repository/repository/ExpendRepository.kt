package br.com.apps.repository.repository

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EXPENDS
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toExpendList
import br.com.apps.repository.util.toExpendObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

private const val PAID_BY_EMPLOYEE = "paidByEmployee"
private const val ALREADY_REFUNDED = "alreadyRefunded"

class ExpendRepository(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)
    private val parentCollection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    private val write = ExpWrite(fireStore)
    private val read = ExpRead(fireStore)

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    suspend fun delete(expendId: String) = write.delete(expendId)

    suspend fun save(dto: ExpendDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    suspend fun getExpendListByDriverId(driverId: String, flow: Boolean = false) =
        read.getExpendListByDriverId(driverId, flow)

    suspend fun getExpendListByDriverIdsAndRefundableStatus(
        driverIdList: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ) = read.getExpendListByDriverIdsAndRefundableStatus(
        driverIdList,
        paidByEmployee,
        alreadyRefunded,
        flow
    )

    suspend fun getExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ) = read.getExpendListByDriverIdAndRefundableStatus(
        driverId,
        paidByEmployee,
        alreadyRefunded,
        flow
    )

    suspend fun getExpendListByTravelId(travelId: String, flow: Boolean = false) =
        read.getExpendListByTravelId(travelId, flow)

    suspend fun getExpendListByTravelId(idList: List<String>, flow: Boolean = false) =
        read.getExpendListByTravelId(idList, flow)

    suspend fun getExpendById(expendId: String, flow: Boolean = false) =
        read.getExpendById(expendId, flow)

}

private class ExpRead(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)

    /**
     * Fetches the [Expend] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun getExpendListByDriverId(
        driverId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return if (flow) listener.onSnapShot { it.toExpendList() }
        else listener.onComplete { it.toExpendList() }
    }

    /**
     * Fetches the [Expend] dataSet for the specified driver ID list.
     *
     * @param driverIdList The ID list of the [Employee]'s.
     * @param paidByEmployee If the [Expend] was paid by the employee.
     * @param alreadyRefunded If the [Expend] has already been refunded.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun getExpendListByDriverIdsAndRefundableStatus(
        driverIdList: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>> {
        val listener = collection.whereIn(DRIVER_ID, driverIdList)
            .whereEqualTo(PAID_BY_EMPLOYEE, paidByEmployee)
            .whereEqualTo(ALREADY_REFUNDED, alreadyRefunded)

        return if (flow) listener.onSnapShot { it.toExpendList() }
        else listener.onComplete { it.toExpendList() }
    }

    /**
     * Fetches the [Expend] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param paidByEmployee If the [Expend] was paid by the employee.
     * @param alreadyRefunded If the [Expend] has already been refunded.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun getExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)
            .whereEqualTo(PAID_BY_EMPLOYEE, paidByEmployee)
            .whereEqualTo(ALREADY_REFUNDED, alreadyRefunded)

        return if (flow) listener.onSnapShot { it.toExpendList() }
        else listener.onComplete { it.toExpendList() }
    }

    /**
     * Fetches the [Expend] dataSet for the specified travel ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun getExpendListByTravelId(
        travelId: String,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>> {
        val listener = collection.whereEqualTo(TRAVEL_ID, travelId)

        return if (flow) listener.onSnapShot { it.toExpendList() }
        else listener.onComplete { it.toExpendList() }
    }

    /**
     * Fetches the [Expend] dataSet for the specified driver ID list.
     *
     * @param driverIdList The ID list of the [Employee]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun getExpendListByTravelId(
        driverIdList: List<String>,
        flow: Boolean = false
    ): LiveData<Response<List<Expend>>> {
        val listener = collection.whereIn(TRAVEL_ID, driverIdList)

        return if (flow) listener.onSnapShot { it.toExpendList() }
        else listener.onComplete { it.toExpendList() }
    }

    /**
     * Fetches the [Expend] dataSet for the specified ID.
     *
     * @param expendId The ID list of the [Expend].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    suspend fun getExpendById(expendId: String, flow: Boolean = false): LiveData<Response<Expend>> {
        val listener = collection.document(expendId)

        return if (flow) listener.onSnapShot { it.toExpendObject() }
        else listener.onComplete { it.toExpendObject() }
    }

}

private class ExpWrite(fireStore: FirebaseFirestore) {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)

    /**
     * Deletes an [Expend] document from the database based on the specified ID.
     *
     * @param expendId The ID of the expenditure document to be deleted.
     */
    suspend fun delete(expendId: String) {
        collection
            .document(expendId)
            .delete()
            .await()
    }

    /**
     * Saves the [ExpendDto] object.
     * If the ID of the [ExpendDto] is null, it creates a new [Expend].
     * If the ID is not null, it updates the existing [Expend].
     *
     * @param dto The [ExpendDto] object to be saved.
     */
    suspend fun save(dto: ExpendDto) {
        if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: ExpendDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: ExpendDto) {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()

    }

}
