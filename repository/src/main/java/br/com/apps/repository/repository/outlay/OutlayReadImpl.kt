package br.com.apps.repository.repository.outlay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.travel.Outlay
import br.com.apps.repository.util.ALREADY_REFUNDED
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_EXPENDS
import br.com.apps.repository.util.ID
import br.com.apps.repository.util.IS_VALID
import br.com.apps.repository.util.PAID_BY_EMPLOYEE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toExpendObject
import br.com.apps.repository.util.toOutlayList
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OutlayReadImpl(fireStore: FirebaseFirestore) : OutlayReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_EXPENDS)

    override suspend fun fetchOutlayListByDriverId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Outlay>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toOutlayList() }
            false -> listener.onComplete { it.toOutlayList() }
        }
    }

    override suspend fun fetchOutlayListByDriverIdsAndRefundableStatus(
        ids: List<String>,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Outlay>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(EMPLOYEE_ID, ids)
            .whereEqualTo(PAID_BY_EMPLOYEE, paidByEmployee)
            .whereEqualTo(ALREADY_REFUNDED, alreadyRefunded)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toOutlayList() }
            false -> listener.onComplete { it.toOutlayList() }
        }
    }

    override suspend fun fetchOutlayListByDriverIdAndRefundableStatus(
        id: String,
        paidByEmployee: Boolean,
        alreadyRefunded: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Outlay>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)
            .whereEqualTo(PAID_BY_EMPLOYEE, paidByEmployee)
            .whereEqualTo(ALREADY_REFUNDED, alreadyRefunded)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toOutlayList() }
            false -> listener.onComplete { it.toOutlayList() }
        }
    }

    override suspend fun fetchOutlayListByTravelId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Outlay>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(TRAVEL_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toOutlayList() }
            false -> listener.onComplete { it.toOutlayList() }
        }
    }

    override suspend fun fetchOutlayListByTravelIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Outlay>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(TRAVEL_ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toOutlayList() }
            false -> listener.onComplete { it.toOutlayList() }
        }
    }

    override suspend fun fetchOutlayById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Outlay>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toExpendObject() }
            false -> listener.onComplete { it.toExpendObject() }
        }
    }

    override suspend fun fetchOutlayListByDriverIdAndIsNotRefundYet(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Outlay>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)
            .whereEqualTo(IS_VALID, true)
            .whereEqualTo(PAID_BY_EMPLOYEE, true)
            .whereEqualTo(ALREADY_REFUNDED, false)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toOutlayList() }
            false -> listener.onComplete { it.toOutlayList() }
        }
    }

    override suspend fun fetchOutlayByIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Outlay>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toOutlayList() }
            false -> listener.onComplete { it.toOutlayList() }
        }
    }

}

