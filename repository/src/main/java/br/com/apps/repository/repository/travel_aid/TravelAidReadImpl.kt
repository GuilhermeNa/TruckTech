package br.com.apps.repository.repository.travel_aid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_COST_HELP
import br.com.apps.repository.util.ID
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTravelAidList
import br.com.apps.repository.util.validateId
import br.com.apps.repository.util.validateIds
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TravelAidReadImpl(fireStore: FirebaseFirestore) : TravelAidReadInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_COST_HELP)

    override suspend fun fetchTravelAidListByTravelId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(TRAVEL_ID, id)
            .whereEqualTo(IS_PAID, false)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toTravelAidList() }
            false -> listener.onComplete { it.toTravelAidList() }
        }
    }

    override suspend fun fetchTravelAidListByTravelIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> = withContext(Dispatchers.IO) {
        ids.validateIds()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereIn(TRAVEL_ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toTravelAidList() }
            false -> listener.onComplete { it.toTravelAidList() }
        }
    }

    override suspend fun fetchTravelAidListByDriverId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> = withContext(Dispatchers.IO) {
        id.validateId()?.let { error -> return@withContext MutableLiveData(error) }

        val listener = collection.whereEqualTo(EMPLOYEE_ID, id)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toTravelAidList() }
            false -> listener.onComplete { it.toTravelAidList() }
        }
    }

    override suspend fun fetchTravelAidByIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> = withContext(Dispatchers.IO){
        ids.validateIds()?.let { error -> MutableLiveData(error) }

        val listener = collection.whereIn(ID, ids)

        return@withContext when (flow) {
            true -> listener.onSnapShot { it.toTravelAidList() }
            false -> listener.onComplete { it.toTravelAidList() }
        }
    }

}