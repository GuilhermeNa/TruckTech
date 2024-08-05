package br.com.apps.repository.repository.fleet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.model.fleet.Truck
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRUCKS
import br.com.apps.repository.util.FLEET_TYPE
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRUCK_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTrailerList
import br.com.apps.repository.util.toTruckList
import br.com.apps.repository.util.toTruckObject
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FleetReadImpl(fireStore: FirebaseFirestore) : FleetReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRUCKS)

    override suspend fun fetchTruckListByMasterUid(
        uid: String,
        flow: Boolean
    ): LiveData<Response<List<Truck>>> = withContext(Dispatchers.IO) {
        uid.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(MASTER_UID, uid)
            .whereEqualTo(FLEET_TYPE, FleetCategory.TRUCK.toString())

        return@withContext if (flow) listener.onSnapShot { it.toTruckList() }
        else listener.onComplete { it.toTruckList() }
    }

    override suspend fun fetchTruckById(
        id: String,
        flow: Boolean
    ): LiveData<Response<Truck>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext if (flow) listener.onSnapShot { it.toTruckObject() }
        else listener.onComplete { it.toTruckObject() }
    }

    override suspend fun fetchTruckByDriverId(
        id: String,
        flow: Boolean
    ): LiveData<Response<Truck>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(DRIVER_ID, id)
            .whereEqualTo(FLEET_TYPE, FleetCategory.TRUCK.toString()).limit(1)

        return@withContext if (flow) listener.onSnapShot { it.toTruckList()[0] }
        else listener.onComplete { it.toTruckList()[0] }
    }

    override suspend fun fetchTrailerListLinkedToTruckById(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Trailer>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(TRUCK_ID, id)

        return@withContext if(flow) listener.onSnapShot { it.toTrailerList() }
        else listener.onComplete { it.toTrailerList() }
    }

}