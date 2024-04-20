package br.com.apps.usecase

import br.com.apps.repository.repository.ExpendRepository

class ExpendUseCase(private val repository: ExpendRepository) {

    suspend fun deleteExpendForThisTravel(travelId: String, id: String) {
        repository.deleteExpendForThisTravel(travelId, id)
    }

}