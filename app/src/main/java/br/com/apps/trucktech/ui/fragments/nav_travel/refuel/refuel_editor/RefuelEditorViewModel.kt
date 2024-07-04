package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.factory.RefuelFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.model.toDate
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.RefuelUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class RefuelEditorViewModel(
    private val vmData: RefuelEVMData,
    private val repository: RefuelRepository,
    private val useCase: RefuelUseCase
) : ViewModel() {

    private var isEditing: Boolean = vmData.refuelId?.let { true } ?: false

    /**
     * LiveData containing the [LocalDateTime] of the [Refuel] date,
     * or LocalDateTime.now() if there is no [Refuel] to be loaded.
     */
    private val _date = MutableLiveData<LocalDateTime>()
    val date get() = _date

    /**
     * if there is an [Refuel] to be loaded, this liveData is
     * responsible for holding the [Response] data of the [Refuel].
     */
    private val _data = MutableLiveData<Response<Refuel>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        if (isEditing) loadData()
        else _date.value = LocalDateTime.now()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getRefuelById(vmData.refuelId!!).asFlow().first { response ->
                    when (response) {
                        is Response.Error -> _data.value = response
                        is Response.Success -> {
                            _data.value = response
                            _date.value = response.data?.date ?: LocalDateTime.now()
                        }
                    }
                true
            }
        }
    }

    /**
     * Send the [Refuel] to be created or updated.
     */
    fun save(viewDto: RefuelDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val writeReq = WriteRequest(
                    authLevel = vmData.permission,
                    data = createOrUpdate(viewDto)
                )
                useCase.save(writeReq)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

    private fun createOrUpdate(viewDto: RefuelDto): RefuelDto {
        viewDto.apply {
            date = _date.value!!.toDate()
        }

        return when(isEditing) {
            true -> {
                val refuel = (data.value as Response.Success).data!!
                RefuelFactory.update(refuel, viewDto)
                refuel.toDto()
            }
            false -> {
                viewDto.apply {
                    masterUid = vmData.masterUid
                    truckId = vmData.truckId
                    travelId = vmData.travelId
                    driverId = vmData.driverId
                    isValid = false
                }
                RefuelFactory.create(viewDto).toDto()
            }
        }
    }

    /**
     * Interact with the [_date] LiveData, changing it.
     */
    fun newDateHaveBeenSelected(dateInLong: Long) {
        val datetime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(dateInLong), ZoneId.systemDefault())
        _date.value = datetime
    }

}

data class RefuelEVMData(
    val masterUid: String,
    val truckId: String,
    val travelId: String,
    val driverId: String,
    val refuelId: String? = null,
    val permission: PermissionLevelType
)