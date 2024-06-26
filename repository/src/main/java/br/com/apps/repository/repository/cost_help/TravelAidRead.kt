package br.com.apps.repository.repository.cost_help

import androidx.lifecycle.LiveData
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.util.EMPLOYEE_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_COST_HELP
import br.com.apps.repository.util.IS_PAID
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.TRAVEL_ID
import br.com.apps.repository.util.onComplete
import br.com.apps.repository.util.onSnapShot
import br.com.apps.repository.util.toTravelAidList
import com.google.firebase.firestore.FirebaseFirestore

class TravelAidRead(fireStore: FirebaseFirestore) : TravelAidReadI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_COST_HELP)

    override suspend fun getCostHelpByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> {
        val listener = collection.whereEqualTo(EMPLOYEE_ID, employeeId)
            .whereEqualTo(IS_PAID, false)

        return if(flow) listener.onSnapShot { it.toTravelAidList() }
        else listener.onComplete { it.toTravelAidList() }
    }

    override suspend fun getTravelAidListByTravelId(
        travelId: String,
        flow: Boolean
    ): LiveData<Response<List<TravelAid>>> {
         val listener = collection.whereEqualTo(TRAVEL_ID, travelId)
            .whereEqualTo(IS_PAID, false)

        return if(flow) listener.onSnapShot { it.toTravelAidList() }
        else listener.onComplete { it.toTravelAidList() }
    }

}