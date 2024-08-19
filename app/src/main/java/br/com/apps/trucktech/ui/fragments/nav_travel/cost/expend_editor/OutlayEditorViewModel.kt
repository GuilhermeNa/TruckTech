package br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.null_objects.NullExpendException
import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.expressions.atBrZone
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.Label.Companion.getIdByName
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.util.toDate
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.OutlayUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

class OutlayEditorViewModel(
    private val vmData: OutlayEVMData,
    private val expendRepository: OutlayRepository,
    private val labelRepository: LabelRepository,
    private val useCase: OutlayUseCase
) : ViewModel() {

    private var isEditing: Boolean = vmData.expendId?.let { true } ?: false

    /**
     * LiveData containing the [LocalDateTime] of the [Outlay] date,
     * or LocalDateTime.now() if there is no [Outlay] to be loaded.
     */
    private val _date = MutableLiveData<LocalDateTime>()
    val date get() = _date

    /**
     * if there is an [Outlay] to be loaded, this liveData is
     * responsible for holding the [Response] data of the [Outlay].
     */
    private val _data = MutableLiveData<OutlayEFData>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init { fetchData() }

    /**
     * Loads data asynchronously.
     *  1. Load the [Label] list.
     *  2. Load the [Outlay] if there is an ID to be loaded.
     */
    private fun fetchData() {
        suspend fun fetchLabels(): List<Label> {
            val response =
                labelRepository.getAllOperationalLabelListForDrivers(vmData.masterUid)
                    .asFlow().first()
            return when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data ?: throw NullLabelException(UNKNOWN_EXCEPTION)
            }
        }

        suspend fun fetchOutlays(expendId: String): Outlay {
            val response = expendRepository.fetchOutlayById(expendId)
                .asFlow().first()
            return when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data ?: throw NullExpendException(UNKNOWN_EXCEPTION)
            }
        }

        fun sendResponse(labels: List<Label>, nOutlay: Outlay?) {
            if (isEditing) {
                nOutlay!!.setLabelById(labels)
                setDate(nOutlay.date)
                setFragmentData(OutlayEFData(labelList = labels, outlay = nOutlay))

            } else {
                setDate(LocalDateTime.now().atBrZone())
                setFragmentData(OutlayEFData(labelList = labels))

            }
        }

        viewModelScope.launch {
            try {
                val labels = fetchLabels()
                val nExpend = vmData.expendId?.let { fetchOutlays(it) }
                sendResponse(labels, nExpend)

            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
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

    /**
     * Send the [Outlay] to be created or updated.
     */
    fun save(viewDto: OutlayDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val writeReq = WriteRequest(
                    authLevel = vmData.permission,
                    data = generateExpend(viewDto)
                )
                useCase.save(writeReq)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

    private fun generateExpend(viewDto: OutlayDto): OutlayDto {
        fun setCommonFields() {
            viewDto.apply {
                masterUid = vmData.masterUid
                truckId = vmData.truckId
                travelId = vmData.travelId
                employeeId = vmData.driverId
            }
        }

        fun whenEditing(): OutlayDto {
            val expend = data.value!!.outlay!!
            setCommonFields()
            return viewDto.also {
                it.isValid = expend.isValid
                it.id = expend.id
            }
        }

        fun whenCreating(): OutlayDto {
            setCommonFields()
            return viewDto.also { it.isValid = false }
        }

        return if(isEditing) whenEditing()
        else whenCreating()

    }

    private fun setDate(date: LocalDateTime) {
        _date.value = date
    }

    private fun setFragmentData(data: OutlayEFData) {
        _data.value = data
    }

    fun getDate(): Date = date.value!!.toDate()

    fun getLabelId(name: String): String = data.value!!.labelList.getIdByName(name)!!

}

class OutlayEVMData(
    val masterUid: String,
    val truckId: String,
    val driverId: String,
    val travelId: String,
    val expendId: String? = null,
    val permission: AccessLevel
)

class OutlayEFData(
    val labelList: List<Label>,
    val outlay: Outlay? = null
)