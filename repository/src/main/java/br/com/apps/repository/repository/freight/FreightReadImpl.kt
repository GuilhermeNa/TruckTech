package br.com.apps.repository.repository.freight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FREIGHTS
import br.com.apps.repository.util.IS_COMMISSION_PAID
import br.com.apps.repository.util.IS_VALID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toFreightList
import br.com.apps.repository.util.toFreightObject
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FreightReadImpl(fireStore: FirebaseFirestore) : FreightReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    override suspend fun fetchFreightListByDriverId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)

        return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    override suspend fun fetchFreightListByDriverIdsAndPaymentStatus(
        ids: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener =
            collection.whereIn(EMPLOYEE_ID, ids).whereEqualTo(IS_COMMISSION_PAID, isPaid)

        return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    override suspend fun fetchFreightListByDriverIdAndPaymentStatus(
        id: String,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection
            .whereEqualTo(EMPLOYEE_ID, id)
            .whereEqualTo(IS_COMMISSION_PAID, isPaid)

        return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    override suspend fun fetchFreightListByTravelId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(TRAVEL_ID, id)

        return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    override suspend fun fetchFreightListByTravelIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(TRAVEL_ID, ids)

        return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

    override suspend fun fetchFreightById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Freight>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext if (flow) listener.onSnapShot { it.toFreightObject() }
        else listener.onComplete { it.toFreightObject() }
    }

    override suspend fun fetchFreightListByDriverIdAndIsNotPaidYet(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection
            .whereEqualTo(EMPLOYEE_ID, id)
            .whereEqualTo(IS_VALID, true)
            .whereEqualTo(IS_COMMISSION_PAID, false)

        return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
        else listener.onComplete { it.toFreightList() }
    }

}