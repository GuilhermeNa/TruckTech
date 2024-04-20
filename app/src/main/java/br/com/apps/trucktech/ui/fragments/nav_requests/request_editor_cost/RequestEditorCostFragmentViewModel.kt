package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.Label
import br.com.apps.model.model.LabelType
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.Resource
import br.com.apps.usecase.LabelUseCase
import br.com.apps.usecase.RequestUseCase
import kotlinx.coroutines.launch

class RequestEditorCostFragmentViewModel(
    private val masterUid: String?,
    private val requestId: String,
    private val itemId: String?,
    private val requestUseCase: RequestUseCase,
    private val labelUseCase: LabelUseCase
) : ViewModel() {

    var requestItem: RequestItem? = null

    /**
     * Loaded request
     */
    private val _loadedRequest = MutableLiveData<Resource<PaymentRequest>>()
    val loadedRequest get() = _loadedRequest

    /**
     * Loaded labels
     */
    private val _loadedLabelsData = MutableLiveData<Resource<List<Label>>>()
    val loadedLabelsData get() = _loadedLabelsData

    /**
     * Item saved
     */
    private val _requestItemSaved = MutableLiveData<Resource<Boolean>>()
    val requestItemSaved get() = _requestItemSaved

    /**
     * Load data
     */
    fun loadData() {
        masterUid?.let {
            viewModelScope.launch {

                labelUseCase.getOperationalLabels(masterUid, LabelType.COST).asFlow()
                    .collect {
                        _loadedLabelsData.value = it

                        requestUseCase.getRequestAndItemsById(requestId).asFlow()
                            .collect { resource ->
                                if (resource.error == null) {
                                    requestItem = itemId?.let { id ->
                                        resource.data.itemsList?.firstOrNull { item -> item.id == id }
                                    }
                                }
                                _loadedRequest.value = resource
                            }

                    }
            }
        }
    }

    /**
     * Save button
     */
    fun saveButtonClicked(requestItemDto: RequestItemDto) {
        viewModelScope.launch {
            requestUseCase.saveItem(requestId, requestItemDto).asFlow().collect {
                _requestItemSaved.value = it
            }
        }
    }

    fun getListOfTitles(labels: List<Label>): List<String> {
        return labelUseCase.getListOfTitles(labels)
    }

    fun getLabelDescription(): String? {
        val labelId = requestItem?.labelId
        val labelsList = loadedLabelsData.value?.data//TODO nulo pq esse valor ainda n foi atribuido
        val label = labelsList?.firstOrNull { it.id == labelId }
        return label?.name
    }

    fun getLabelIdByName(autoCompleteText: String): String? {
        var labelId: String? = null
        loadedLabelsData.value?.data?.let { labelsList ->
            val label = labelsList.firstOrNull { it.name == autoCompleteText }
            labelId = label?.id
        }
        return labelId
    }

}