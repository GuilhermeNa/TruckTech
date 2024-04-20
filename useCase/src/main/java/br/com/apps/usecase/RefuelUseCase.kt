package br.com.apps.usecase

import br.com.apps.repository.repository.RefuelRepository

class RefuelUseCase(private val repository: RefuelRepository ) {

    suspend fun deleteRefuelForThisTravel(travelId: String, id: String) {
        repository.deleteRefuelForThisTravel(travelId, id)
    }

}