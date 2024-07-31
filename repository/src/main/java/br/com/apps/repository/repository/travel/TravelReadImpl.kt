package br.com.apps.repository.repository.travel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.util.IS_FINISHED
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTravelList
import br.com.apps.repository.util.toTravelObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TravelReadImpl(fireStore: FirebaseFirestore) : TravelReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    override suspend fun fetchTravelListByDriverIdAndIsFinished(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<Travel>>> = withContext(Dispatchers.IO) {
        if (driverId.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener =
            collection.whereEqualTo(DRIVER_ID, driverId).whereEqualTo(IS_FINISHED, true)

        return@withContext if (flow) listener.onSnapShot { it.toTravelList() }
        else listener.onComplete { it.toTravelList() }
    }

    override suspend fun fetchTravelListByDriverId(
        driverId: String,
        flow: Boolean
    ): LiveData<Response<List<Travel>>> = withContext(Dispatchers.IO) {
        if (driverId.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return@withContext if (flow) listener.onSnapShot { it.toTravelList() }
        else listener.onComplete { it.toTravelList() }
    }

    override suspend fun fetchTravelById(
        travelId: String,
        flow: Boolean
    ): LiveData<Response<Travel>> = withContext(Dispatchers.IO) {
        if (travelId.isEmpty())
            return@withContext MutableLiveData(Response.Error(EmptyDataException("Id cannot be empty")))

        val listener = collection.document(travelId)

        return@withContext if (flow) listener.onSnapShot { it.toTravelObject() }
        else listener.onComplete { it.toTravelObject() }
    }


}