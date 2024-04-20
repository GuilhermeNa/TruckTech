package br.com.apps.usecase

import androidx.lifecycle.LiveData
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.EMPTY_DATASET
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.Response
import br.com.apps.repository.repository.RefuelRepository
import java.security.InvalidParameterException

class RefuelUseCase(private val repository: RefuelRepository ) {

    fun mergeRefuelList(travelList: List<Travel>, refuelListNullable: List<Refuel>?) {
        val refuelList = refuelListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val refuels = refuelList.filter { it.travelId == travelId }
            travel.refuelsList = refuels
        }
    }

    suspend fun getRefuelForThisTravelId(idList: List<String>): LiveData<Response<List<Refuel>>> {
        return repository.getRefuelListForThisTravel(idList)
    }

    suspend fun deleteRefuelForThisTravel(travelId: String, id: String) {
        repository.deleteRefuelForThisTravel(travelId, id)
    }

}