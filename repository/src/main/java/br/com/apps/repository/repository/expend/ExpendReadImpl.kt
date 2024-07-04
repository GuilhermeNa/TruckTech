package br.com.apps.repository.repository.expend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.ALREADY_REFUNDED
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EXPENDS
import br.com.apps.repository.util.IS_VALID
import br.com.apps.repository.util.PAID_BY_EMPLOYEE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toExpendList
import br.com.apps.repository.util.toExpendObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpendReadImpl(fireStore: FirebaseFirestore) : ExpendReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)

    override suspend fun getExpendListByDriverId(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)
            return@withContext if (flow) listener.onSnapShot { it.toExpendList() }
            else listener.onComplete { it.toExpendList() }
        }
    }

    override suspend fun getExpendListByDriverIdsAndRefundableStatus(
        driverIdList: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            if (driverIdList.isEmpty())
                return@withContext MutableLiveData(Response.Error(EmptyIdException("ExpendReadImpl: emptyId")))

            val listener = collection.whereIn(DRIVER_ID, driverIdList)
                .whereEqualTo(PAID_BY_EMPLOYEE, paidByEmployee)
                .whereEqualTo(ALREADY_REFUNDED, alreadyRefunded)

            return@withContext if (flow) listener.onSnapShot { it.toExpendList() }
            else listener.onComplete { it.toExpendList() }
        }
    }

    override suspend fun getExpendListByDriverIdAndRefundableStatus(
        driverId: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)
                .whereEqualTo(PAID_BY_EMPLOYEE, paidByEmployee)
                .whereEqualTo(ALREADY_REFUNDED, alreadyRefunded)

            return@withContext if (flow) listener.onSnapShot { it.toExpendList() }
            else listener.onComplete { it.toExpendList() }
        }
    }

    override suspend fun getExpendListByTravelId(
        travelId: String,
        flow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.whereEqualTo(TRAVEL_ID, travelId)

            return@withContext if (flow) listener.onSnapShot { it.toExpendList() }
            else listener.onComplete { it.toExpendList() }
        }
    }

    override suspend fun getExpendListByTravelIds(
        travelIdList: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            if (travelIdList.isEmpty()) return@withContext MutableLiveData(Response.Success(emptyList()))

            val listener = collection.whereIn(TRAVEL_ID, travelIdList)

            return@withContext if (flow) listener.onSnapShot { it.toExpendList() }
            else listener.onComplete { it.toExpendList() }
        }
    }

    override suspend fun getExpendById(
        expendId: String,
        flow: Boolean
    ): LiveData<Response<Expend>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.document(expendId)

            return@withContext if (flow) listener.onSnapShot { it.toExpendObject() }
            else listener.onComplete { it.toExpendObject() }
        }
    }

    override suspend fun getExpendListByDriverIdAndIsNotRefundYet(
        driverId: String, flow: Boolean
    ): LiveData<Response<List<Expend>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)
                .whereEqualTo(IS_VALID, true)
                .whereEqualTo(PAID_BY_EMPLOYEE, true)
                .whereEqualTo(ALREADY_REFUNDED, false)

            return@withContext if (flow) listener.onSnapShot { it.toExpendList() }
            else listener.onComplete { it.toExpendList() }
        }
    }

}