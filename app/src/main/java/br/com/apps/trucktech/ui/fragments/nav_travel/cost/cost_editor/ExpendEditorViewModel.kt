package br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.factory.ExpendFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.ExpendRepository
import br.com.apps.repository.repository.LabelRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import java.security.InvalidParameterException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ExpendEditorViewModel(
    private val idHolder: IdHolder,
    private val expendRepository: ExpendRepository,
    private val labelRepository: LabelRepository
) : ViewModel() {

    /**
     * The [Expend] to be loaded when there is an ID.
     */
    private lateinit var expend: Expend

    /**
     * The [Label] list with drivers operational data.
     */
    lateinit var labelList: List<Label>

    /**
     * LiveData containing the [LocalDateTime] of the [Expend] date,
     * or LocalDateTime.now() if there is no [Expend] to be loaded.
     */
    private val _date = MutableLiveData<LocalDateTime>()
    val date get() = _date

    /**
     * LiveData holding the response data of type [Response] with the [Label] data.
     */
    private val _labelData = MutableLiveData<Response<List<Label>>>()
    val labelData get() = _labelData

    /**
     * if there is an [Expend] to be loaded, this liveData is
     * responsible for holding the [Response] data of the [Expend].
     */
    private val _expendData = MutableLiveData<Response<Expend>>()
    val expendData get() = _expendData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    /**
     * Loads data asynchronously.
     *  1. Load the [Label] list.
     *  2. Load the [Expend] if there is an ID to be loaded.
     */
    private fun loadData() {
        viewModelScope.launch {
            val deferredA = CompletableDeferred<Unit>()
            launch { loadLabelData(deferredA) }

            if (idHolder.expendId != null) {
                deferredA.await()
                loadExpendData(idHolder.expendId!!)
            } else {
                _date.value = LocalDateTime.now()
            }
        }
    }

    private suspend fun loadLabelData(deferredA: CompletableDeferred<Unit>) {
        val masterUid = idHolder.masterUid ?: throw InvalidParameterException(EMPTY_ID)
        val liveData = labelRepository.getAllOperationalLabelListForDrivers(masterUid)
        liveData.asFlow().collect { response ->
            when (response) {
                is Response.Error -> _labelData.value = response
                is Response.Success -> {
                    response.data?.let { labelData ->
                        labelList = labelData
                        _labelData.value = response
                    }
                }
            }
            deferredA.complete(Unit)
        }
    }

    private suspend fun loadExpendData(id: String) {
        expendRepository.getExpendById(id, false).asFlow().collect { response ->
            when (response) {
                is Response.Error -> _expendData.value = response
                is Response.Success -> {
                    response.data?.let { expend ->
                        expend.date?.let { _date.value = it }
                        expend.label = labelList.firstOrNull { it.id == expend.labelId }
                        this.expend = expend
                        _expendData.value = response
                    }
                }
            }
        }
    }

    private fun getExpendDto(mappedFields: HashMap<String, String>): ExpendDto {
        return if (::expend.isInitialized) {
            expend.label = null
            ExpendFactory.update(expend, mappedFields)
            expend.toDto()
        } else {
            mappedFields[ExpendFactory.TAG_MASTER_UID] = idHolder.masterUid!!
            mappedFields[ExpendFactory.TAG_TRAVEL_ID] = idHolder.travelId!!
            mappedFields[ExpendFactory.TAG_DRIVER_ID] = idHolder.driverId!!
            mappedFields[ExpendFactory.TAG_TRUCK_ID] = idHolder.truckId!!
            ExpendFactory.create(mappedFields).toDto()
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

    /**
     * Send the [Expend] to be created or updated.
     */
    fun save(mappedFields: HashMap<String, String>) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = getExpendDto(mappedFields)
                expendRepository.save(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

}