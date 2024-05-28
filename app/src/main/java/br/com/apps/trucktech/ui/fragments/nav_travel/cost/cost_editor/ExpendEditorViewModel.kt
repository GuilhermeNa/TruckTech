package br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.factory.ExpendFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.toDate
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.Response
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ExpendEditorViewModel(
    private val vmData: ExpendEVMData,
    private val expendRepository: ExpendRepository,
    private val labelRepository: LabelRepository
) : ViewModel() {

    private var isEditing: Boolean = vmData.expendId?.let { true } ?: false

    /**
     * LiveData containing the [LocalDateTime] of the [Expend] date,
     * or LocalDateTime.now() if there is no [Expend] to be loaded.
     */
    private val _date = MutableLiveData<LocalDateTime>()
    val date get() = _date

    /**
     * if there is an [Expend] to be loaded, this liveData is
     * responsible for holding the [Response] data of the [Expend].
     */
    private val _data = MutableLiveData<ExpendEFData>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData { labels, expend ->
            processData(labels, expend)
            sendResponse(labels, expend)
        }
    }

    /**
     * Loads data asynchronously.
     *  1. Load the [Label] list.
     *  2. Load the [Expend] if there is an ID to be loaded.
     */
    private fun loadData(complete: (labels: List<Label>, expend: Expend?) -> Unit) {
        viewModelScope.launch {
            val labelResponse = loadLabels()
            val labels = labelResponse.await()

            val expendResponse = vmData.expendId?.let { loadExpend(it) }
            val expend = expendResponse?.await()

            complete(labels, expend)
        }
    }

    private suspend fun loadLabels(): CompletableDeferred<List<Label>> {
        val deferred = CompletableDeferred<List<Label>>()

        labelRepository.getAllOperationalLabelListForDrivers(vmData.masterUid)
            .asFlow().first { response ->
                when (response) {
                    is Response.Error -> deferred.completeExceptionally(response.exception)
                    is Response.Success -> {
                        response.data?.let { deferred.complete(it) }
                            ?: deferred.completeExceptionally(NullPointerException(EMPTY_DATASET))
                    }
                }
                true
            }

        return deferred
    }

    private suspend fun loadExpend(expendId: String): CompletableDeferred<Expend> {
        val deferred = CompletableDeferred<Expend>()

        expendRepository.getExpendById(expendId).asFlow().first { response ->
            when (response) {
                is Response.Error -> deferred.completeExceptionally(response.exception)
                is Response.Success -> {
                    response.data?.let { deferred.complete(it) }
                        ?: deferred.completeExceptionally(NullPointerException(EMPTY_DATASET))
                }
            }
            true
        }

        return deferred
    }

    private fun processData(labels: List<Label>, expend: Expend?) {
        _date.value = expend?.date ?: LocalDateTime.now()

        expend?.let { it.label = labels.firstOrNull { l -> l.id == it.labelId } }

    }

    private fun sendResponse(labels: List<Label>, expend: Expend?) {
        _data.value = ExpendEFData(labelList = labels, expend = expend)
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
    fun save(viewDto: ExpendDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = createOrUpdate(viewDto)
                expendRepository.save(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private fun createOrUpdate(viewDto: ExpendDto): ExpendDto {
        viewDto.apply {
            date = _date.value!!.toDate()
            label = null
        }

        return when(isEditing) {
            true -> {
                val expend = _data.value!!.expend!!
                ExpendFactory.update(expend, viewDto)
                expend.toDto()
            }

            false -> {
                viewDto.apply {
                    masterUid = vmData.masterUid
                    truckId = vmData.truckId
                    travelId = vmData.travelId
                    driverId = vmData.driverId
                    isAlreadyRefunded = false
                }
                ExpendFactory.create(viewDto).toDto()
            }
        }
    }

}

class ExpendEVMData(
    val masterUid: String,
    val truckId: String,
    val driverId: String,
    val travelId: String,
    val expendId: String? = null
)

class ExpendEFData(
    val labelList: List<Label>,
    val expend: Expend?
)