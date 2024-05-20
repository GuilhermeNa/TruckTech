package br.com.apps.repository.repository.expend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.ALREADY_REFUNDED
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EXPENDS
import br.com.apps.repository.util.PAID_BY_EMPLOYEE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toExpendList
import br.com.apps.repository.util.toExpendObject
import com.google.firebase.firestore.FirebaseFirestore

class ExpendRead(fireStore: FirebaseFirestore): ExpendReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)

    /**
     * Fetches the [Expend] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Expend] list.
     */
    override suspend fun getExpendListByDriverId(
        driverId: String,
        flow: Boolean
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
    override suspend fun getExpendListByDriverIdsAndRefundableStatus(
        driverIdList: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Expend>>> {
        if (driverIdList.isEmpty())
            return MutableLiveData(Response.Error(EmptyIdException("ExpendRead: emptyId")))

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
    override suspend fun getExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
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
    override suspend fun getExpendListByTravelId(
        travelId: String,
        flow: Boolean
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
    override suspend fun getExpendListByTravelIds(
        driverIdList: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Expend>>> {
        if (driverIdList.isEmpty())
            return MutableLiveData(Response.Error(EmptyIdException("ExpendRead: emptyId")))

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
    override suspend fun getExpendById(expendId: String, flow: Boolean): LiveData<Response<Expend>> {
        val listener = collection.document(expendId)

        return if (flow) listener.onSnapShot { it.toExpendObject() }
        else listener.onComplete { it.toExpendObject() }
    }

}