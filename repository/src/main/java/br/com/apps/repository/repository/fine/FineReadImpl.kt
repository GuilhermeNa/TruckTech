package br.com.apps.repository.repository.fine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.FleetFine
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_FINES
import br.com.apps.repository.util.FLEET_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toFineList
import br.com.apps.repository.util.toFineObject
import br.com.apps.repository.util.validateId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FineReadImpl(fireStore: FirebaseFirestore) : FineReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_FINES)

    override suspend fun fetchFineListByDriverId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<FleetFine>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)

        return@withContext if (flow) listener.onSnapShot { it.toFineList() }
        else listener.onComplete { it.toFineList() }
    }

    override suspend fun fetchFineListByFleetId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<FleetFine>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(FLEET_ID, id)

        return@withContext if (flow) listener.onSnapShot { it.toFineList() }
        else listener.onComplete { it.toFineList() }
    }

    override suspend fun fetchFineById(
        id: String,
        flow: Boolean
    ): LiveData<Response<FleetFine>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.document(id)

        return@withContext if (flow) listener.onSnapShot { it.toFineObject() }
        else listener.onComplete { it.toFineObject() }
    }

}