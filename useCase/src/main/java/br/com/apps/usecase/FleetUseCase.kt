package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.TruckDto
import br.com.apps.model.mapper.TruckMapper
import br.com.apps.model.model.Truck
import br.com.apps.repository.repository.FleetRepository
import com.google.firebase.firestore.toObject

class FleetUseCase(private val repository: FleetRepository) {

    /**
     * add a new truck
     */
    fun save(truckDto: TruckDto): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        repository.save(truckDto)
        liveData.value = true
        return liveData
    }

    /**
     * delete by id
     */
    fun delete(truckId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        repository.delete(truckId)
        liveData.value = true
        return liveData
    }

    /**
     * Get all trucks for this user
     */
    fun getAll(): LiveData<List<Truck>> {
        return repository.getAll()
    }

    /**
     * get a truck by id
     */
    fun getById(truckId: String): LiveData<Truck> {
        val liveData = MutableLiveData<Truck>()
        repository.getById(truckId)
            .addSnapshotListener { s, _ ->
                s?.let { document ->
                    document.toObject<TruckDto>()?.let { truckDto ->
                        TruckMapper.toModel(truckDto, document.id)
                    }?.let {
                        liveData.value = it
                    }
                }
            }
        return liveData
    }

    fun getByDriverId(driverId: String): LiveData<Truck> {
        return repository.getByDriverId(driverId)
    }

}