package br.com.apps.trucktech.ui.fragments.nav_travel.travel_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.util.Response
import br.com.apps.model.expressions.atBrZone
import br.com.apps.usecase.usecase.TravelUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TravelPreviewViewModel(
    private val vmData: TravelPreviewVmData,
    private val useCase: TravelUseCase
) : ViewModel() {

    private var _isFirstBoot = true
    val isFirstBoot get() = _isFirstBoot

    private val _state = MutableLiveData<StateTP>()
    val state get() = _state

    private val _data = MutableLiveData<Travel>()
    val data get() = _data

    private var _appBarState = MutableLiveData<StateAppBarTP>()
    val appBarState get() = _appBarState

    //---------------------------------------------------------------------------------------------//
    // INIT
    //---------------------------------------------------------------------------------------------//

    init {
        setState(StateTP.Loading)
    }

    fun loadData() {
        viewModelScope.launch {
            useCase.getTravelById(vmData.travelId).asFlow().first { response ->
                when (response) {
                    is Response.Error -> {
                        response.exception.printStackTrace()
                        setState(StateTP.Error(response.exception))
                    }

                    is Response.Success -> {
                        _state.value = (response.data?.let { t ->

                            _data.value = t

                            if (t.isEmptyTravel()) {
                                StateTP.Empty
                            } else if (t.isFinished) {
                                StateTP.Loaded.AlreadyFinished
                            } else {
                                if (t.isReadyToBeFinished()) StateTP.Loaded.ReadyForAuth
                                else StateTP.Loaded.AwaitingAuth
                            }

                        } ?: StateTP.Error(NullPointerException()))
                    }
                }

                if (isFirstBoot) _isFirstBoot = false
                true
            }
        }
    }

    fun setState(state: StateTP) {
        _state.value = state
    }

    fun setAppBarState(state: StateAppBarTP) {
        _appBarState.value = state
    }

    fun endTravel() = liveData<Response<Unit>>(viewModelScope.coroutineContext) {
        setState(StateTP.Finishing)

        try {
            val travel = data.value!!
            val dto = travel.apply {
                finalOdometerMeasurement = refuelsList?.last()?.odometerMeasure
                finalDate = LocalDateTime.now().atBrZone()
                isFinished = true
                considerAverage = travel.shouldConsiderAverage()
                validateForSaving()
            }.toDto()
            useCase.setTravelFinished(vmData.permission, dto)
            setState(StateTP.Loaded.AlreadyFinished)
            emit(Response.Success())

        } catch (e: Exception) {
            setState(StateTP.Loaded.ReadyForAuth)
            emit(Response.Error(e))
        }
    }

}

sealed class StateTP {
    object Loading : StateTP()
    sealed class Loaded : StateTP() {
        object AlreadyFinished : Loaded()
        object AwaitingAuth : Loaded()
        object ReadyForAuth : Loaded()
    }

    object Empty : StateTP()
    data class Error(val error: Exception) : StateTP()

    object Finishing : StateTP()

}

sealed class StateAppBarTP {
    object IsDisabled : StateAppBarTP()
    object Prepare : StateAppBarTP()
    sealed class IsEnabled : StateAppBarTP() {
        object Expanded : IsEnabled()
        object Contracted : IsEnabled()
    }
}

data class TravelPreviewVmData(
    val travelId: String,
    val permission: PermissionLevelType
)





