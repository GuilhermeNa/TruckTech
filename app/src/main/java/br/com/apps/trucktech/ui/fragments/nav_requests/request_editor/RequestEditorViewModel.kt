package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RequestEditorViewModel(
    private val requestId: String,
    private val repository: RequestRepository
) : ViewModel() {

    private val _boxPaymentImage = MutableLiveData<String>()
    val boxPaymentImage get() = _boxPaymentImage

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
            sendResponse(request)
        }
    }

    fun loadData(complete: (request: PaymentRequest, items: List<RequestItem>) -> Unit) {
        viewModelScope.launch {
            val request = loadRequest()
            loadItems { items -> complete(request, items) }
        }
    }

    private suspend fun loadRequest(): PaymentRequest {
        val deferred = CompletableDeferred<PaymentRequest>()

        repository.getRequestById(requestId).asFlow().first { response ->
            when (response) {
                is Response.Error -> _data.value = response
                is Response.Success -> {
                    if (response.data != null) deferred.complete(response.data!!)
                    else _data.value = Response.Error(NullPointerException())
                }
            }
            true
        }

        return deferred.await()
    }

    private suspend fun loadItems(complete: (List<RequestItem>) -> Unit) {
        repository.getItemListByRequestId(requestId, true).asFlow().collect { response ->
            when (response) {
                is Response.Error -> _data.value = response
                is Response.Success -> {
                    if (response.data != null) complete(response.data!!)
                    else _data.value = Response.Error(NullPointerException())
                }
            }
        }

    }

    private fun sendResponse(request: PaymentRequest) {
        if(_boxPaymentImage.value == null) {
            request.encodedImage?.let { _boxPaymentImage.value = it }
        }
        _data.value = Response.Success(request)
    }

    fun deleteItem(itemId: String) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                repository.deleteItem(requestId, itemId)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
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

    fun imageHaveBeenLoaded(encodedImage: String) {
        _boxPaymentImage.value = encodedImage
    }

    suspend fun saveEncodedImage() {
        val encodedImage = _boxPaymentImage.value
            ?: throw NullPointerException("Não é possível salvar a imagem, seu valor é nulo")
        repository.updateEncodedImage(requestId = requestId, encodedImage)
    }

}