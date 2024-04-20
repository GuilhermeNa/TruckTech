package br.com.apps.trucktech.ui.fragments.nav_travel.travels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.Response
import br.com.apps.usecase.TravelIdsData
import br.com.apps.usecase.TravelUseCase
import kotlinx.coroutines.launch

class TravelsListViewModel(
    private val driverId: String,
    private val useCase: TravelUseCase
) : ViewModel() {

    private val _travelData = MutableLiveData<Response<List<Travel>>>()
    val travelData get() = _travelData

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            useCase.getCompleteTravelsListByDriverId(driverId).asFlow().collect {
                _travelData.postValue(it)
            }
        }
    }

    suspend fun delete(idsData: TravelIdsData) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                useCase.deleteTravel(idsData)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }


}

