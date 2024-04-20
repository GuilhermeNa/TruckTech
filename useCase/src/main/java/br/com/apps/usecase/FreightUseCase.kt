package br.com.apps.usecase

import br.com.apps.repository.repository.FreightRepository

class FreightUseCase(private val repository: FreightRepository) {

    suspend fun deleteFreightForThisTravel(travelId: String, id: String) {
        repository.deleteFreightForThisTravel(travelId, id)

    }
}
