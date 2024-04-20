package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.repository.Resource
import br.com.apps.usecase.RequestUseCase
import kotlinx.coroutines.launch

class RequestEditorFragmentViewModel(private val useCase: RequestUseCase) : ViewModel() {

    var requestId: String? = ""

    /**
     * Loaded data
     */
    private val _loadedRequestData = MutableLiveData<Resource<PaymentRequest>>()
    val loadedRequestData get() = _loadedRequestData

    /**
     * Bottom Sheet visibility
     */
    private var _bottomSheetVisibility = MutableLiveData(false)
    val bottomSheetVisibility get() = _bottomSheetVisibility

    /**
     * Item deleted
     */
    private var _requestItemDeleted = MutableLiveData<Resource<Boolean>>()
    val requestItemDeleted get() = _requestItemDeleted

    fun bottomSheetRequested() {
        _bottomSheetVisibility.value = true
    }

    fun bottomSheetDismissed() {
        _bottomSheetVisibility.value = false
    }

    fun loadData() {
        viewModelScope.launch {
            requestId?.let { id ->
                useCase
                    .getRequestAndItemsById(id)
                    .asFlow()
                    .collect {
                        _loadedRequestData.value = it
                    }
            } ?: run {
                _loadedRequestData.value =
                    Resource(data = PaymentRequest(), error = "Invalid request id")
            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {

            requestId?.let {
                useCase.deleteItem(it, itemId).asFlow().collect {

                }
            }

        }
    }

}