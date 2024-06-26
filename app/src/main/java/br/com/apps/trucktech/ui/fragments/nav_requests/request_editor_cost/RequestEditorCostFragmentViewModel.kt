package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.factory.RequestItemFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.LabelType
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.request.travel_requests.RequestItemType
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.LabelUseCase
import br.com.apps.usecase.RequestUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RequestEditorCostFragmentViewModel(
    private val vmData: RequestEditorCostVmData,
    private val labelUseCase: LabelUseCase,
    private val repository: RequestRepository,
    private val useCase: RequestUseCase
) : ViewModel() {

    private var isEditing: Boolean = vmData.costReqId?.let { true } ?: false

    private val _data = MutableLiveData<RequestEditorCostFData>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val labels = loadLabels()
            val item = vmData.costReqId?.let { loadItem(it) }

            _data.value = RequestEditorCostFData(
                labels = labels,
                item = item
            )

        }
    }

    private suspend fun loadLabels(): List<Label> {
        val deferred = CompletableDeferred<List<Label>>()

        labelUseCase.getOperationalLabels(vmData.masterUid, LabelType.COST, true)
            .asFlow().first { response ->
                val data = when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data ?: throw NullPointerException()
                }
                deferred.complete(data)
            }

        return deferred.await()
    }

    private suspend fun loadItem(itemId: String): RequestItem {
        val deferred = CompletableDeferred<RequestItem>()

        repository.getItemById(vmData.requestId, itemId)
            .asFlow().first { response ->
                val data = when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data ?: throw NullPointerException()
                }
                deferred.complete(data)
            }

        return deferred.await()
    }

    /**
     * Transforms the mapped fields into a DTO. Creates a new [RequestItem] or updates it if it already exists.
     */
    fun save(viewDto: RequestItemDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = createOrUpdate(viewDto)
                useCase.saveItem(vmData.permission, dto)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

    private fun createOrUpdate(viewDto: RequestItemDto): RequestItemDto {
        return when (isEditing) {
            true -> {
                val item = data.value!!.item!!
                RequestItemFactory.update(item, viewDto)
                item.toDto()
            }

            false -> {
                viewDto.requestId = vmData.requestId
                RequestItemFactory.create(viewDto, RequestItemType.COST).toDto()
            }
        }
    }

    /**
     * Retrieves the description of the label associated with the request item.
     * Returns the name of the label if found, or null otherwise.
     */
    fun getLabelDescriptionById(): String? {
        val labelId = data.value!!.item!!.labelId
        val labelsList = data.value!!.labels
        val label = labelsList.firstOrNull { it.id == labelId }
        return label?.name
    }

    /**
     * Retrieves the ID of the label by its name from the autoCompleteText.
     * Returns the ID of the label if found, or an empty string if not found.
     */
    fun getLabelIdByName(autoCompleteText: String): String {
        var labelId = ""
        val labelList = data.value!!.labels
        labelList.let { labelsList ->
            val label = labelsList.firstOrNull { it.name == autoCompleteText }
            labelId = label?.id.toString()
        }
        return labelId
    }

}

data class RequestEditorCostVmData(
    val masterUid: String,
    val requestId: String,
    val costReqId: String? = null,
    val permission: PermissionLevelType
)

data class RequestEditorCostFData(
    val labels: List<Label>,
    val item: RequestItem? = null
)