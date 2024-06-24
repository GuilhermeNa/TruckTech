package br.com.apps.trucktech.ui.fragments.nav_travel.travels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.toDate
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.buildUiResponse
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.TravelUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TravelsListViewModel(
    private val vmData: TravelLVMData,
    private val useCase: TravelUseCase,
    private val repository: TravelRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of [Travel]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Travel>>()
    val data get() = _data

    private val _state = MutableLiveData<State>()
    val state get() = _state

    private val _dialog = MutableLiveData<Boolean>()
    val dialog get() = _dialog

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    fun loadData() {
        viewModelScope.launch {
            useCase.getCompleteTravelListByDriverId(vmData.driverId).asFlow().first { response ->
                response.buildUiResponse(_state, _data)
                true
            }
        }
    }

    suspend fun delete(travel: Travel) =

        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            setState(State.Deleting)

            try {
                useCase.deleteTravel(travel)
                setState(State.Deleted)
                emit(Response.Success())

            } catch (e: Exception) {
                setState(State.Deleted)
                emit(Response.Error(e))

            }
        }

    private fun setState(state: State) {
        _state.value = state
    }

    fun createAndSave() = liveData<Response<Unit>>(viewModelScope.coroutineContext) {
        try {
            val dto = createDto()
            repository.save(dto)
            emit(Response.Success())
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }

    private fun createDto(): TravelDto {
        return TravelDto(
            masterUid = vmData.masterUid,
            truckId = vmData.truckId,
            driverId = vmData.driverId,
            isFinished = false,
            initialDate = LocalDateTime.now().toDate(),
            initialOdometerMeasurement = 349592.0
        )
    }

    //TODO criar o objeto da forma correta

    fun dialogDismissed() {
        _dialog.value = false
    }

    fun dialogRequested() {
        _dialog.value = true
    }

}

data class TravelLVMData(
    val masterUid: String,
    val driverId: String,
    val truckId: String
)