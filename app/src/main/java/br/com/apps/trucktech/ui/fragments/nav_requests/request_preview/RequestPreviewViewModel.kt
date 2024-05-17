package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.RequestRepository
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import kotlinx.coroutines.launch

class RequestPreviewViewModel(
    private val requestId: String,
    private val repository: RequestRepository
) : ViewModel() {

    private lateinit var itemIdList: List<String>

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
                when(response) {
                    is Response.Error -> _requestData.value = response
                    is Response.Success -> {
                        response.data?.let {
                            itemIdList = it.itemsList!!.map { it.id!! }.toList()
                        }
                        _requestData.value = response
                    }
                }
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
                repository.delete(requestId, itemIdList)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

}