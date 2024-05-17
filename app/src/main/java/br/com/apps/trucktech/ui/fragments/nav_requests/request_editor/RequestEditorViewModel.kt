package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.RequestRepository
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
    private val _requestData = MutableLiveData<Response<PaymentRequest>>()
    val requestData get() = _requestData

    /**
     * LiveData with a dark layer state, used when dialogs and bottom sheets are requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getCompleteRequestById(requestId, true).asFlow().collect { response ->
                when (response) {
                    is Response.Error -> _requestData.value = response
                    is Response.Success -> {
                        response.data?.let { request ->
                            if (!request.encodedImage.isNullOrBlank()) {
                                _boxPaymentImage.value = request.encodedImage!!
                            }
                        }
                        _requestData.value = response
                    }
                }
            }
        }
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