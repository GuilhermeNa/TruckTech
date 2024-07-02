package br.com.apps.repository.repository.travel_aid

import androidx.lifecycle.LiveData
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.util.Response

interface TravelAidI : TravelAidReadI

interface TravelAidReadI {

    suspend fun getCostHelpByDriverIdAndIsNotDiscountedYet(
        employeeId: String,
        flow: Boolean = false
    ): LiveData<Response<List<TravelAid>>>

    suspend fun getTravelAidListByTravelId(travelId: String, flow: Boolean = false)
            : LiveData<Response<List<TravelAid>>>

    suspend fun getTravelAidListByTravelIds(travelIdList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<TravelAid>>>

}