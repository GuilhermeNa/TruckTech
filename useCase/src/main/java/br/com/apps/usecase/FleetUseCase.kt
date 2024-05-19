package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.TruckDto
import br.com.apps.repository.repository.FleetRepository

class FleetUseCase(private val repository: FleetRepository) {

    /**
     * add a new truck
     */
    suspend fun save(truckDto: TruckDto): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        repository.save(truckDto)
        liveData.value = true
        return liveData
    }

    /**
     * delete by id
     */
    suspend fun delete(truckId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        repository.delete(truckId)
        liveData.value = true
        return liveData
    }

}