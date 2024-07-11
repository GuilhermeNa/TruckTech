package br.com.apps.repository.repository.travel_aid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_COST_HELP
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTravelAidList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TravelAidReadImpl(fireStore: FirebaseFirestore) : TravelAidReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_COST_HELP)

    override suspend fun getTravelAidByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.whereEqualTo(EMPLOYEE_ID, employeeId)
                .whereEqualTo(IS_PAID, false)
            return@withContext if (flow) listener.onSnapShot { it.toTravelAidList() }
            else listener.onComplete { it.toTravelAidList() }
        }
    }

    override suspend fun getTravelAidListByTravelId(
        travelId: String,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> {
        return withContext(Dispatchers.IO) {
            val listener = collection.whereEqualTo(TRAVEL_ID, travelId)
                .whereEqualTo(IS_PAID, false)

            return@withContext if (flow) listener.onSnapShot { it.toTravelAidList() }
            else listener.onComplete { it.toTravelAidList() }
        }
    }

    override suspend fun getTravelAidListByTravelIds(
        travelIdList: List<String>,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> {
        return withContext(Dispatchers.IO) {
            if (travelIdList.isEmpty()) return@withContext MutableLiveData(Response.Success(emptyList()))

            val listener = collection.whereIn(TRAVEL_ID, travelIdList)

            return@withContext if (flow) listener.onSnapShot { it.toTravelAidList() }
            else listener.onComplete { it.toTravelAidList() }
        }
    }

    override suspend fun getTravelAidListByDriverId(driverId: String, flow: Boolean)
    : LiveData<Response<List<TravelAid>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)
        return if (flow) listener.onSnapShot { it.toTravelAidList() }
        else listener.onComplete { it.toTravelAidList() }
    }

}