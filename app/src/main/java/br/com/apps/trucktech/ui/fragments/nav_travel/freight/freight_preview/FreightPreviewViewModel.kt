package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.null_objects.NullCustomerException
import br.com.apps.model.exceptions.null_objects.NullFreightException
import br.com.apps.model.model.Customer
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.FreightUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FreightPreviewViewModel(
    private val vmData: FreightPreviewVmData,
    private val freightRepository: FreightRepository,
    private val customerRepository: CustomerRepository,
    private val useCase: FreightUseCase
) : ViewModel() {

    private lateinit var customers: List<Customer>

    /**
     * LiveData holding the response data of type [Response] with a [Freight]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<Freight>>()
    val data get() = _data

    /**
     * LiveData with a dark layer state, used when dialog is requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    private val _writeAuth = MutableLiveData<Boolean>()
    val writeAuth get() = _writeAuth

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            customers = loadCustomers()
            loadFreightFlow { fre ->
                sendResponse(fre)
            }
        }
    }

    private suspend fun loadCustomers(): List<Customer> {
        val response = customerRepository.fetchCustomerListByMasterUid(vmData.masterUid)
            .asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullCustomerException(UNKNOWN_EXCEPTION)
        }
    }

    private suspend fun loadFreightFlow(onComplete: (freight: Freight) -> Unit) {
        freightRepository.fetchFreightById(id = vmData.freightId, flow = true)
            .asFlow().collect { response ->
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data?.let { onComplete(it) }
                        ?: throw NullFreightException(UNKNOWN_EXCEPTION)
                }
            }
    }

    private fun sendResponse(freight: Freight) {
        setWriteAuth(!freight.isValid)
        freight.setCustomerById(customers)
        setFragmentData(freight)
    }

    fun delete() = liveData<Response<Unit>>(viewModelScope.coroutineContext) {
        try {
            val writeReq = WriteRequest(
                authLevel = vmData.permission,
                data = (data.value as Response.Success).data!!.toDto()
            )
            useCase.delete(writeReq)
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

    private fun setWriteAuth(hasAuth: Boolean) {
        _writeAuth.value = hasAuth
    }

    private fun setFragmentData(freight: Freight) {
        _data.value = Response.Success(freight)
    }

}

data class FreightPreviewVmData(
    val masterUid: String,
    val freightId: String,
    val permission: AccessLevel
)