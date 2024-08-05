package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.exceptions.null_objects.NullRefuelException
import br.com.apps.model.expressions.atBrZone
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.user.AccessLevel
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.RefuelUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

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
        else setDate(LocalDateTime.now().atBrZone())
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val ref = loadRefuel()
                setDate(ref.date.atBrZone())
                setFragmentData(Response.Success(ref))

            } catch (e: Exception) {
                setFragmentData(Response.Error(e))

            }
        }
    }

    private suspend fun loadRefuel(): Refuel {
        val response = repository.fetchRefuelById(vmData.refuelId!!)
            .asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullRefuelException(UNKNOWN_EXCEPTION)
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
                    data = generateDto(viewDto)
                )
                useCase.save(writeReq)
                emit(Response.Success())

            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))

            }
        }

    private fun generateDto(viewDto: RefuelDto): RefuelDto {
        fun setCommonFields() {
            viewDto.run {
                masterUid = vmData.masterUid
                truckId = vmData.truckId
                travelId = vmData.travelId
            }
        }

        fun whenEditing(): RefuelDto {
            val refuel = (data.value as Response.Success).data!!
            setCommonFields()

            return viewDto.apply {
                id = refuel.id
                isValid = refuel.isValid
            }
        }

        fun whenCreating(): RefuelDto {
            setCommonFields()

            return viewDto.apply {
                isValid = false
            }
        }

        return if(isEditing) whenEditing()
        else whenCreating()
    }

    /**
     * Interact with the [_date] LiveData, changing it.
     */
    fun newDateHaveBeenSelected(dateInLong: Long) {
        val datetime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dateInLong), ZoneOffset.UTC
        )
        _date.value = datetime
    }

    private fun setDate(date: LocalDateTime) {
        _date.value = date
    }

    private fun setFragmentData(response: Response<Refuel>) {
        _data.value = response
    }

}

data class RefuelEVMData(
    val masterUid: String,
    val truckId: String,
    val travelId: String,
    val driverId: String,
    val refuelId: String? = null,
    val permission: AccessLevel
)