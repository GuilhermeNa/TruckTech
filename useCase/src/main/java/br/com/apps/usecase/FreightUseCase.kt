package br.com.apps.usecase

import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.EMPTY_DATASET
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.repository.FreightRepository
import java.security.InvalidParameterException

class FreightUseCase(
    private val repository: FreightRepository,
    private val labelUseCase: LabelUseCase
) {

    /**
     * Merges the provided list of freights into the corresponding travels in the travel list.
     * Each freight is associated with a travel based on the travel ID.
     *
     * @param travelList The list of travels into which freights will be merged.
     * @param freightListNullable The nullable list of freights to merge into the travels.
     * @throws InvalidParameterException if the provided freight list is null.
     * @throws InvalidParameterException if any travel in the travel list has a null ID.
     */
    fun mergeFreightList(travelList: List<Travel>, freightListNullable: List<Freight>?) {
        val freightList = freightListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val freights = freightList.filter { it.travelId == travelId }
            travel.freightsList = freights
        }
    }

    suspend fun delete(freightId: String) {
        repository.delete(freightId)
    }

}
