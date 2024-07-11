package br.com.apps.repository.repository.freight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.model.model.travel.Freight
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FreightReadImpl(fireStore: FirebaseFirestore) : FreightReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FREIGHTS)

    override suspend fun getFreightListByDriverId(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.whereEqualTo(DRIVER_ID, driverId)
            return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
            else listener.onComplete { it.toFreightList() }
        }
    }

    override suspend fun getFreightListByDriverIdsAndPaymentStatus(
        driverIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            if (driverIdList.isEmpty())
                return@withContext MutableLiveData(Response.Error(EmptyIdException("FreightReadImpl: emptyId")))

            val listener = collection
                .whereIn(DRIVER_ID, driverIdList)
                .whereEqualTo(IS_COMMISSION_PAID, isPaid)

            return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
            else listener.onComplete { it.toFreightList() }
        }
    }

    override suspend fun getFreightListByDriverIdAndPaymentStatus(
        driverId: String,
        isPaid: Boolean,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection
                .whereEqualTo(DRIVER_ID, driverId)
                .whereEqualTo(IS_COMMISSION_PAID, isPaid)

            return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
            else listener.onComplete { it.toFreightList() }
        }
    }

    override suspend fun getFreightListByTravelId(
        travelId: String,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.whereEqualTo(TRAVEL_ID, travelId)
            return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
            else listener.onComplete { it.toFreightList() }
        }
    }

    override suspend fun getFreightListByTravelIds(
        travelIdList: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            if (travelIdList.isEmpty()) return@withContext MutableLiveData(Response.Success(emptyList()))
            val listener = collection.whereIn(TRAVEL_ID, travelIdList)
            return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
            else listener.onComplete { it.toFreightList() }
        }
    }

    override suspend fun getFreightById(
        freightId: String,
        flow: Boolean
    ): LiveData<Response<Freight>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.document(freightId)

            return@withContext if (flow) listener.onSnapShot { it.toFreightObject() }
            else listener.onComplete { it.toFreightObject() }
        }
    }

    override suspend fun getFreightListByDriverIdAndIsNotPaidYet(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<Freight>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection
                .whereEqualTo(DRIVER_ID, driverId)
                .whereEqualTo(IS_VALID, true)
                .whereEqualTo(IS_COMMISSION_PAID, false)

            return@withContext if (flow) listener.onSnapShot { it.toFreightList() }
            else listener.onComplete { it.toFreightList() }
        }
    }

}