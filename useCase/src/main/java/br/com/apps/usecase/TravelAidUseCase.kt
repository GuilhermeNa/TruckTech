package br.com.apps.usecase

import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.repository.cost_help.TravelAidRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.EMPTY_ID
import java.security.InvalidParameterException

class TravelAidUseCase(private val repository: TravelAidRepository) {

    fun mergeAidList(travelList: List<Travel>, aidListNullable: List<TravelAid>?) {
        val aidList = aidListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val aid = aidList.filter { it.travelId == travelId }
            travel.aidList = aid
        }
    }

}