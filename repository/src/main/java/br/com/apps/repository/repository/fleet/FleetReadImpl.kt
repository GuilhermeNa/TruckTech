package br.com.apps.repository.repository.fleet

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Truck
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRUCKS
import br.com.apps.repository.util.MASTER_UID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTruckList
import br.com.apps.repository.util.toTruckObject
import com.google.firebase.firestore.FirebaseFirestore

class FleetReadImpl(fireStore: FirebaseFirestore) : FleetReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRUCKS)

    override suspend fun getTruckListByMasterUid(masterUid: String, flow: Boolean)
            : LiveData<Response<List<Truck>>> {
        val listener = collection.whereEqualTo(MASTER_UID, masterUid)
        return if (flow) listener.onSnapShot { it.toTruckList() }
        else listener.onComplete { it.toTruckList() }
    }

    override suspend fun getTruckById(truckId: String, flow: Boolean)
            : LiveData<Response<Truck>> {
        val listener = collection.document(truckId)
        return if (flow) listener.onSnapShot { it.toTruckObject() }
        else listener.onComplete { it.toTruckObject() }
    }

    override suspend fun getTruckByDriverId(driverId: String, flow: Boolean)
            : LiveData<Response<Truck>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId).limit(1)
        return if (flow) listener.onSnapShot { it.toTruckList()[0] }
        else listener.onComplete { it.toTruckList()[0] }
    }

}