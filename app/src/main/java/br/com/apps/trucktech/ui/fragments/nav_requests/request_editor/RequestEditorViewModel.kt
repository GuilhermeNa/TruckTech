package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.RequestUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RequestEditorViewModel(
    private val vmData: RequestEditorVmData,
    private val repository: RequestRepository,
    private val useCase: RequestUseCase
) : ViewModel() {

    private val _boxPaymentImage = MutableLiveData<String>()
    val boxPaymentImage get() = _boxPaymentImage

    /**
     * LiveData holding the response data of type [Response] with a [PaymentRequest]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<PaymentRequest>>()
    val data get() = _data

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

        repository.getRequestById(vmData.requestId).asFlow().first { response ->
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
        repository.getItemListByRequestId(vmData.requestId, true).asFlow().collect { response ->
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
        if (_boxPaymentImage.value == null) {
            request.encodedImage?.let { _boxPaymentImage.value = it }
        }
        _data.value = Response.Success(request)
    }

    fun deleteItem(itemId: String) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = getRequestDto()
                useCase.deleteItem(vmData.permission, dto, itemId)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
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
        val dto = getRequestDto()
        val encodedImage = _boxPaymentImage.value
            ?: throw NullPointerException("Não é possível salvar a imagem, seu valor é nulo")
        useCase.updateEncodedImage(vmData.permission, dto, encodedImage)
    }

    private fun getRequestDto(): TravelRequestDto {
        return (data.value as Response.Success).data!!.toDto()
    }

}

data class RequestEditorVmData(
    val requestId: String,
    val permission: PermissionLevelType
)