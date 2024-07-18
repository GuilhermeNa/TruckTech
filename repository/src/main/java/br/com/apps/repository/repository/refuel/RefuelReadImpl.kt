package br.com.apps.repository.repository.refuel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REFUELS
import br.com.apps.repository.util.ODOMETER_MEASURE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toRefuelList
import br.com.apps.repository.util.toRefuelObject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefuelReadImpl(fireStore: FirebaseFirestore) : RefuelReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REFUELS)

    override suspend fun getRefuelListByDriverId(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<Refuel>>> = withContext(Dispatchers.IO) {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    override suspend fun getRefuelListByTravelId(
        travelId: String,
        flow: Boolean
    ): LiveData<Response<List<Refuel>>> = withContext(Dispatchers.IO) {
        val listener = collection.whereEqualTo(TRAVEL_ID, travelId)
            .orderBy(ODOMETER_MEASURE, Query.Direction.ASCENDING)

        if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    override suspend fun getRefuelListByTravelIds(
        idList: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Refuel>>> = withContext(Dispatchers.IO) {
        if (idList.isEmpty()) return@withContext MutableLiveData(Response.Success(emptyList()))

        val listener = collection.whereIn(TRAVEL_ID, idList)

        if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    override suspend fun getRefuelById(
        refuelId: String,
        flow: Boolean
    ): LiveData<Response<Refuel>> = withContext(Dispatchers.IO) {
        val listener = collection.document(refuelId)

        if (flow) listener.onSnapShot { it.toRefuelObject() }
        else listener.onComplete { it.toRefuelObject() }
    }

}
