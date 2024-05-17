package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.factory.RequestItemFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.LabelType
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.model.model.request.request.RequestItemType
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.RequestRepository
import br.com.apps.usecase.LabelUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class RequestEditorCostFragmentViewModel(
    private val idHolder: IdHolder,
    private val labelUseCase: LabelUseCase,
    private val repository: RequestRepository
) : ViewModel() {

    val masterUid = idHolder.masterUid ?: throw NullPointerException("Null masterUid")
    val requestId = idHolder.requestId ?: throw NullPointerException("Null requestId")

    /**
     * Holds a [RequestItem] when the data is loaded, to be saved when necessary.
     */
    lateinit var requestItem: RequestItem

    /**
     * LiveData holding the response data of type [Response] with a list of [Label]
     * to be displayed on screen.
     */
    private val _labelData = MutableLiveData<Response<List<Label>>>()
    val labelData get() = _labelData

    /**
     * LiveData holding the response data of type [Response] with a list of [RequestItem]
     * to be displayed on screen.
     */
    private val _itemData = MutableLiveData<Response<RequestItem>>()
    val itemData get() = _itemData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadData() {
        viewModelScope.launch {
            val deferredA = CompletableDeferred<Unit>()
            val deferredB = CompletableDeferred<RequestItem?>()

            launch { loadLabelData { deferredA.complete(Unit) } }
            launch { loadItemData { deferredB.complete(it) } }

            awaitAll(deferredA, deferredB)

            deferredB.getCompleted()?.let {
                _itemData.value = Response.Success(it)
            }

        }
    }

    private fun loadLabelData(complete: () -> Unit) {
        viewModelScope.launch {
            labelUseCase.getOperationalLabels(masterUid, LabelType.COST, true).asFlow()
                .collect {
                    _labelData.value = it
                    complete()
                }
        }
    }

    private fun loadItemData(complete: (data: RequestItem?) -> Unit) {
        viewModelScope.launch {
            idHolder.expendId?.let { itemId ->
                repository.getItemById(requestId, itemId).asFlow().collect { response ->
                    when (response) {
                        is Response.Error -> _itemData.value = response
                        is Response.Success -> {
                            response.data?.let {
                                requestItem = it
                                complete(it)
                            }
                        }
                    }
                }
            } ?: complete(null)
        }
    }

    /**
     * Transforms the mapped fields into a DTO. Creates a new [RequestItem] or updates it if it already exists.
     */
    fun save(mappedFields: HashMap<String, String>) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = getItemDto(mappedFields)
                repository.saveItem(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private fun getItemDto(mappedFields: HashMap<String, String>): RequestItemDto {
        return if (::requestItem.isInitialized) {
            RequestItemFactory.update(requestItem, mappedFields)
            requestItem.toDto()
        } else {
            mappedFields[RequestItemFactory.TAG_REQUEST_ID] = requestId
            RequestItemFactory.create(mappedFields, RequestItemType.COST).toDto()
        }
    }

    /**
     * Retrieves the description of the label associated with the request item.
     * Returns the name of the label if found, or null otherwise.
     */
    fun getLabelDescriptionById(): String? {
        val labelId = requestItem.labelId
        val labelsList = (labelData.value as Response.Success<List<Label>>).data
        val label = labelsList?.firstOrNull { it.id == labelId }
        return label?.name
    }

    /**
     * Retrieves the ID of the label by its name from the autoCompleteText.
     * Returns the ID of the label if found, or an empty string if not found.
     */
    fun getLabelIdByName(autoCompleteText: String): String {
        var labelId = ""
        val labelList = (labelData.value as Response.Success).data
        labelList?.let { labelsList ->
            val label = labelsList.firstOrNull { it.name == autoCompleteText }
            labelId = label?.id.toString()
        }
        return labelId
    }

}