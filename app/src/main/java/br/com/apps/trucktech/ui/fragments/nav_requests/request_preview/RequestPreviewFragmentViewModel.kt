package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.repository.Resource
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.usecase.RequestUseCase
import kotlinx.coroutines.launch

class RequestPreviewFragmentViewModel(
    private val requestId: String,
    private val useCase: RequestUseCase
) : ViewModel() {

    private val _loadedPaymentRequest = MutableLiveData<Resource<PaymentRequest>>()
    val loadedPaymentRequest get() = _loadedPaymentRequest

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

    fun loadData() {
        viewModelScope.launch {
            useCase.getRequestAndItemsById(requestId).asFlow().collect {
                _loadedPaymentRequest.value = it
            }
        }
    }

}