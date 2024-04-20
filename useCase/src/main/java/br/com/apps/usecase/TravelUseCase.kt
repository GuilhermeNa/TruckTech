package br.com.apps.usecase

import androidx.lifecycle.LiveData
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.Response
import br.com.apps.repository.repository.TravelRepository
import java.io.Serializable

class TravelUseCase(
    private val repository: TravelRepository,
    private val freightUseCase: FreightUseCase,
    private val refuelUseCase: RefuelUseCase,
    private val expendUseCase: ExpendUseCase
) {

    suspend fun getCompleteTravelsListByDriverId(driverId: String): LiveData<Response<List<Travel>>> {
        return repository.getCompleteTravelsListByDriverId(driverId)
    }

    suspend fun deleteTravel(idsData: TravelIdsData) {
        val travelId = idsData.travelId

        idsData.freightIds.forEach { id ->
            freightUseCase.deleteFreightForThisTravel(travelId, id)
        }
        idsData.refuelIds.forEach { id ->
            refuelUseCase.deleteRefuelForThisTravel(travelId, id)
        }
        idsData.expendIds.forEach { id ->
            expendUseCase.deleteExpendForThisTravel(travelId, id)
        }

        repository.deleteTravel(travelId)
    }

}

data class TravelIdsData(
    val travelId: String,
    val freightIds: List<String>,
    val refuelIds: List<String>,
    val expendIds: List<String>
) : Serializable