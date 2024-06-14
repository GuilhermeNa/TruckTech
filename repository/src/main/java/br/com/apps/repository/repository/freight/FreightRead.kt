package br.com.apps.repository.repository.freight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FREIGHTS
import br.com.apps.repository.util.IS_COMMISSION_PAID
import br.com.apps.repository.util.IS_VALID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toFreightList
import br.com.apps.repository.util.toFreightObject
import com.google.firebase.firestore.FirebaseFirestore

class FreightRead(fireStore: FirebaseFirestore) : FreightReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    /**
     * Fetches the [Freight] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Freight] list.
     */
    override suspend fun getFreightListByDriverId(
        driverId: String,
        flow: Boolean
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
    override suspend fun getFreightListByDriverIdsAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        if (driverIdList.isEmpty())
            return MutableLiveData(Response.Error(EmptyIdException("FreightRead: emptyId")))

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
    override suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        flow: Boolean
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
    override suspend fun getFreightListByTravelId(
        travelId: String,
        flow: Boolean
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
    override suspend fun getFreightListByTravelIds(
        travelIdList: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        if (travelIdList.isEmpty()) return MutableLiveData(Response.Success(emptyList()))

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
    override suspend fun getFreightById(
        freightId: String,
        flow: Boolean
    ): LiveData<Response<Freight>> {
        val listener = collection.document(freightId)

        return if (flow) listener.onSnapShot { it.toFreightObject() }
        else listener.onComplete { it.toFreightObject() }
    }

    override suspend fun getFreightListByDriverIdAndIsNotPaidYet(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        val listener = collection
            .whereEqualTo(DRIVER_ID, driverId)
            .whereEqualTo(IS_VALID, true)
            .whereEqualTo(IS_COMMISSION_PAID, false)

        return if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

}