package br.com.apps.repository.repository.refuel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.DRIVER_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REFUELS
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toRefuelList
import br.com.apps.repository.util.toRefuelObject
import com.google.firebase.firestore.FirebaseFirestore

class RefuelRead(fireStore: FirebaseFirestore) : RefuelReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REFUELS)

    /**
     * Fetches the [Refuel] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel] list.
     */
    override suspend fun getRefuelListByDriverId(driverId: String, flow: Boolean)
            : LiveData<Response<List<Refuel>>> {
        val listener = collection.whereEqualTo(DRIVER_ID, driverId)

        return if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    /**
     * Fetches the [Refuel] dataSet for the specified travel ID.
     *
     * @param travelId The ID of the [Travel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Travel] list.
     */
    override suspend fun getRefuelListByTravelId(travelId: String, flow: Boolean)
            : LiveData<Response<List<Refuel>>> {
        val listener = collection.whereEqualTo(TRAVEL_ID, travelId)

        return if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    /**
     * Fetches the [Refuel] dataSet for the specified travel IDs.
     *
     * @param idList The ID of the [Travel]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel] list.
     */
    override suspend fun getRefuelListByTravelIds(idList: List<String>, flow: Boolean)
            : LiveData<Response<List<Refuel>>> {
        if (idList.isEmpty())
            return MutableLiveData(Response.Error(EmptyIdException("RefuelRead: emptyId")))

        val listener = collection.whereIn(TRAVEL_ID, idList)

        return if (flow) listener.onSnapShot { it.toRefuelList() }
        else listener.onComplete { it.toRefuelList() }
    }

    /**
     * Fetches the [Refuel] dataSet for the specified ID.
     *
     * @param refuelId The ID of the [Refuel].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Refuel].
     */
    override suspend fun getRefuelById(refuelId: String, flow: Boolean)
            : LiveData<Response<Refuel>> {
        val listener = collection.document(refuelId)

        return if (flow) listener.onSnapShot { it.toRefuelObject() }
        else listener.onComplete { it.toRefuelObject() }
    }

}