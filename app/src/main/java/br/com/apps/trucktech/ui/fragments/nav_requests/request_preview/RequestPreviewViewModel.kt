package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class RequestPreviewViewModel(
    private val requestId: String,
    private val repository: RequestRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [PaymentRequest]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<PaymentRequest>>()
    val requestData get() = _data

    /**
     * LiveData with a dark layer state, used when dialogs and bottom sheets are requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData { request, items ->
            request.itemsList = items.toMutableList()
            _data.postValue(Response.Success(request))
        }
    }

    fun loadData(complete: (requests: PaymentRequest, items: List<RequestItem>) -> Unit) {
        var isFirstBoot = true
        lateinit var request: PaymentRequest
        lateinit var items: List<RequestItem>

        viewModelScope.launch {
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

        }
    }

    private suspend fun loadRequest(complete: (requests: PaymentRequest) -> Unit) {
        repository.getRequestById(requestId, true).asFlow().collect { response ->
            when (response) {
                is Response.Error -> _data.value = response
                is Response.Success -> {
                    if (response.data != null) complete(response.data!!)
                    else _data.value = Response.Error(NullPointerException())
                }
            }
        }
    }

    private suspend fun loadItems(complete: (items: List<RequestItem>) -> Unit) {
        repository.getItemListByRequestId(requestId, true).asFlow().collect { response ->
            when (response) {
                is Response.Error -> _data.value = response
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
                val itemsId = (_data.value as Response.Success).data!!.itemsList!!.mapNotNull { it.id }
                repository.delete(requestId, itemsId)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

}