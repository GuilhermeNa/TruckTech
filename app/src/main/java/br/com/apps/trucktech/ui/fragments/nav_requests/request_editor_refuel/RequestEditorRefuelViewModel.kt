package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.Resource
import br.com.apps.usecase.RequestUseCase
import kotlinx.coroutines.launch

class RequestEditorRefuelViewModel(
    private val requestId: String,
    private val itemId: String?,
    private val useCase: RequestUseCase
): ViewModel() {

    /**
     * Item object
     */
    var requestItem: RequestItem? = null

    /**
     * Load data
     */
    private val _loadedRequestData = MutableLiveData<Resource<PaymentRequest>>()
    val loadedRequestData get() = _loadedRequestData

    /**
     * Item have been saved
     */
    private val _requestItemSaved = MutableLiveData<Resource<Boolean>>()
    val requestItemSaved get() = _requestItemSaved

    /**
     * Load data
     */
    fun loadData() {
        viewModelScope.launch {
            useCase.getRequestAndItemsById(requestId).asFlow().collect { resource ->
                if(resource.error == null) {
                    requestItem = itemId?.let { id->
                        resource.data.itemsList?.firstOrNull { item -> item.id == id }
                    }
                }
                _loadedRequestData.value = resource
            }
        }
    }

    /**
     * Save item
     */
    fun saveButtonClicked(requestItemDto: RequestItemDto) {
        viewModelScope.launch {
            useCase.saveItem(requestId, requestItemDto).asFlow().collect {
                _requestItemSaved.value = it
            }
        }
    }

}
