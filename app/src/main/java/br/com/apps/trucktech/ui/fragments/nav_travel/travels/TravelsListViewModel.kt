package br.com.apps.trucktech.ui.fragments.nav_travel.travels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.expressions.atBrZone
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.util.toDate
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.TravelUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class TravelsListViewModel(
    private val vmData: TravelLVMData,
    private val useCase: TravelUseCase,
    private val repository: TravelRepository
) : ViewModel() {

    private var _isFirstBoot = true
    private val isFirstBoot get() = _isFirstBoot

    private val _state = MutableLiveData<State>()
    val state get() = _state

    private val _dialog = MutableLiveData<Boolean>()
    val dialog get() = _dialog

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
    }

    suspend fun delete(travel: Travel) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                useCase.deleteTravel(travel)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private fun setState(state: State) {
        _state.value = state
    }

    fun createAndSave(odometerMeasurement: Double) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            fun createDto() = TravelDto(
                    masterUid = vmData.masterUid,
                    truckId = vmData.truckId,
                    employeeId = vmData.driverId,
                    isFinished = false,
                    initialDate = LocalDateTime.now().atBrZone().toDate(),
                    isClosed = false,
                    initialOdometer = odometerMeasurement
                )

            try {
                val dto = createDto()
                repository.save(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

    fun setDialog(hasDialog: Boolean) {
        _dialog.value = hasDialog
    }

    suspend fun updateData(data: List<Travel>): List<Travel>? {
        fun processData(data: List<Travel>?): List<Travel>? {
            return when {
                data == null -> {
                    setState(State.Error(NullPointerException()))
                    null
                }

                data.isEmpty() -> {
                    setState(State.Empty)
                    null
                }

                else -> {
                    setState(State.Loaded)
                    data
                }

            }
        }

        if(isFirstBoot) {
            viewModelScope.async { delay(1000) }.await()
            _isFirstBoot = false
        }
        return processData(data)
    }

}

data class TravelLVMData(
    val masterUid: String,
    val driverId: String,
    val truckId: String
)