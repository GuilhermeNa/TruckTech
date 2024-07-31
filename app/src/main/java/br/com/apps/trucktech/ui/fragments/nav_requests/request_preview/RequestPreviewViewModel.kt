package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.PaymentRequestStatusType
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import br.com.apps.model.expressions.getCompleteDateInPtBr
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.RequestUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class RequestPreviewViewModel(
    private val vmData: RequestPreviewVmData,
    private val repository: RequestRepository,
    private val useCase: RequestUseCase
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state get() = _state

    /**
     * LiveData holding the response data of type [Response] with a [PaymentRequest]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<PaymentRequest>()
    val data get() = _data

    private val _menu = MutableLiveData<Boolean>()
    val menu get() = _menu

    /**
     * LiveData with a dark layer state, used when dialogs and bottom sheets are requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)

        loadData { request, items ->
            request.itemsList = items.toMutableList()
            _menu.value = request.status == PaymentRequestStatusType.SENT
            _data.postValue(request)
           setState(State.Loaded)
        }

    }

    private fun setState(state: State) {
        if(_state.value != state) _state.value = state
    }

    private fun loadData(complete: (requests: PaymentRequest, items: List<RequestItem>) -> Unit) {
        var isFirstBoot = true
        lateinit var request: PaymentRequest
        lateinit var items: List<RequestItem>

        viewModelScope.launch {
            try {

                val requestResponse = CompletableDeferred<PaymentRequest>()
                val itemsResponse = CompletableDeferred<List<RequestItem>>()

                launch {
                    loadRequest {
                        request = it
                        if (isFirstBoot) requestResponse.complete(request)
                        else complete(request, items)
                    }
                }

                launch {
                    loadItems {
                        items = it
                        if (isFirstBoot) itemsResponse.complete(items)
                        else complete(request, items)
                    }
                }

                awaitAll(requestResponse, itemsResponse)
                isFirstBoot = false
                complete(request, items)

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))
            }
        }

    }

    private suspend fun loadRequest(complete: (requests: PaymentRequest) -> Unit) {
        repository.fetchRequestById(vmData.requestId, true).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> {
                    if (response.data != null) complete(response.data!!)
                    else throw NullPointerException()
                }
            }
        }
    }

    private suspend fun loadItems(complete: (items: List<RequestItem>) -> Unit) {
        repository.fetchItemListByRequestId(vmData.requestId, true).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data?.let { complete(it) } ?: complete(emptyList())
            }
        }
    }

    fun getDescriptionText(request: PaymentRequest): String {
        val value = request.getTotalValue()
        val date = request.date?.getCompleteDateInPtBr()

        return buildString {
            append("No dia ")
            append(date)
            append(",")
            append(" você fez uma requisição no valor de ")
            append(value.toCurrencyPtBr())
            append(".")
        }

    }

    /**
     * Sets the visibility of the [_darkLayer] to true, indicating that it should be shown.
     */
    fun requestDarkLayer() {
        _darkLayer.value = true
    }

    /**
     * Sets the visibility of the [_darkLayer] to false, indicating that it should be dismissed.
     */
    fun dismissDarkLayer() {
        _darkLayer.value = false
    }

    fun delete() =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                useCase.delete(vmData.permission, data.value!!)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

}

data class RequestPreviewVmData(
    val requestId: String,
    val permission: PermissionLevelType
)