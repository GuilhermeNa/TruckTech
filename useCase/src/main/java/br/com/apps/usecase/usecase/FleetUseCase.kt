package br.com.apps.usecase.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.fleet.TruckDto
import br.com.apps.model.model.fleet.Truck
import br.com.apps.repository.repository.fleet.FleetRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.util.awaitData
import kotlinx.coroutines.coroutineScope

class FleetUseCase(private val repository: FleetRepository) {

    /**
     * Saves a new truck to the repository.
     *
     * @param truckDto The data transfer object representing the truck to be saved.
     * @return LiveData<Boolean> A LiveData object indicating the success of the save operation.
     */
    suspend fun save(truckDto: TruckDto): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        repository.save(truckDto)
        liveData.value = true
        return liveData
    }

    /**
     * Deletes a truck from the repository by its ID.
     *
     * @param truckId The ID of the truck to delete.
     * @return LiveData<Boolean> A LiveData object indicating the success of the delete operation.
     */
    suspend fun delete(truckId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        repository.delete(truckId)
        liveData.value = true
        return liveData
    }

    /**
     * Fetches a truck from the repository by driver ID, including associated trailers.
     *
     * @param driverId The ID of the driver whose truck information is to be fetched.
     * @return LiveData<Response<Truck>> A LiveData object containing the response with the truck information.
     */
    suspend fun fetchTruckByDriverId(driverId: String): LiveData<Response<Truck>> {
        return coroutineScope {
            return@coroutineScope try {
                val truck = repository.fetchTruckByDriverId(driverId).awaitData() ?: throw NullPointerException()
                val trailers = repository.fetchTrailerListLinkedToTruckById(truck.id).awaitData()
                trailers?.forEach { truck.addTrailer(it)  }
                MutableLiveData(Response.Success(truck))
            } catch (e: Exception) {
                MutableLiveData(Response.Error(e))
            }
        }
    }

}