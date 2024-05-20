package br.com.apps.repository.repository.travel

import androidx.lifecycle.LiveData
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_TRAVELS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTravelList
import br.com.apps.repository.util.toTravelObject
import com.google.firebase.firestore.FirebaseFirestore

class TravelRead(fireStore: FirebaseFirestore) : TravelReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_TRAVELS)

    /**
     * Fetches the [Travel] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel] list.
     */
    override suspend fun getTravelListByDriverId(driverId: String, flow: Boolean)
            : LiveData<Response<List<Travel>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return if (flow) listener.onSnapShot { it.toTravelList() }
        else listener.onComplete { it.toTravelList() }
    }

    /**
     * Fetches the [Travel] dataSet for the specified ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel].
     */
    override suspend fun getTravelById(travelId: String, flow: Boolean)
            : LiveData<Response<Travel>> {
        val listener = collection.document(travelId)

        return if (flow) listener.onSnapShot { it.toTravelObject() }
        else listener.onComplete { it.toTravelObject() }
    }

}