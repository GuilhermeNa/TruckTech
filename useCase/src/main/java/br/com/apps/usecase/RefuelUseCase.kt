package br.com.apps.usecase

import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.repository.RefuelRepository
import java.security.InvalidParameterException

class RefuelUseCase(private val repository: RefuelRepository) {

    /**
     * Merges the provided list of refuels into the corresponding travels in the travel list.
     * Each refuel is associated with a travel based on the travel ID.
     *
     * @param travelList The list of travels into which refuels will be merged.
     * @param refuelListNullable The nullable list of refuels to merge into the travels.
     * @throws InvalidParameterException if the provided refuel list is null.
     * @throws InvalidParameterException if any travel in the travel list has a null ID.
     */
    fun mergeRefuelList(travelList: List<Travel>, refuelListNullable: List<Refuel>?) {
        val refuelList = refuelListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val refuels = refuelList.filter { it.travelId == travelId }
            travel.refuelsList = refuels
        }
    }

}