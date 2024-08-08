package br.com.apps.repository.repository.refuel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REFUELS
import br.com.apps.repository.util.ODOMETER_MEASURE
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toRefuelList
import br.com.apps.repository.util.toRefuelObject
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefuelReadImpl(fireStore: FirebaseFirestore) : RefuelReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REFUELS)

    override suspend fun fetchRefuelListByDriverId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Refuel>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRefuelList() }
            false -> listener.onComplete { it.toRefuelList() }
        }
    }

    override suspend fun fetchRefuelListByTravelId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Refuel>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(TRAVEL_ID, id)
            .orderBy(ODOMETER_MEASURE, Query.Direction.ASCENDING)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRefuelList() }
            false -> listener.onComplete { it.toRefuelList() }
        }
    }

    override suspend fun fetchRefuelListByTravelIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Refuel>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(TRAVEL_ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRefuelList() }
            false -> listener.onComplete { it.toRefuelList() }
        }
    }

    override suspend fun fetchRefuelById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Refuel>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toRefuelObject() }
            false -> listener.onComplete { it.toRefuelObject() }
        }
    }

}
