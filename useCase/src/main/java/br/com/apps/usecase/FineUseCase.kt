package br.com.apps.usecase

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Fine
import br.com.apps.repository.repository.FineRepository

class FineUseCase(private val repository: FineRepository) {

    fun addNewFine() {

    }

    fun getById(id: String) {

    }

    fun getAllByDriverId(driverId: String): LiveData<List<Fine>> {
        return repository.getAllByDriverId(driverId)
    }

}