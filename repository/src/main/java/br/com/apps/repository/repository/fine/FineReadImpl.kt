package br.com.apps.repository.repository.fine

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Fine
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FINES
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRUCK_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toFineList
import br.com.apps.repository.util.toFineObject
import com.google.firebase.firestore.FirebaseFirestore

class FineReadImpl(fireStore: FirebaseFirestore) : FineReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FINES)

    override suspend fun getFineListByDriverId(driverId: String, flow: Boolean)
            : LiveData<Response<List<Fine>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)
        return if (flow) listener.onSnapShot { it.toFineList() }
        else listener.onComplete { it.toFineList() }
    }

    override suspend fun getFineListByTruckId(truckId: String, flow: Boolean)
            : LiveData<Response<List<Fine>>> {
        val listener = collection.whereEqualTo(TRUCK_ID, truckId)
        return if (flow) listener.onSnapShot { it.toFineList() }
        else listener.onComplete { it.toFineList() }
    }

    override suspend fun getFineById(fineId: String, flow: Boolean): LiveData<Response<Fine>> {
        val listener = collection.document(fineId)
        return if (flow) listener.onSnapShot { it.toFineObject() }
        else listener.onComplete { it.toFineObject() }
    }

}