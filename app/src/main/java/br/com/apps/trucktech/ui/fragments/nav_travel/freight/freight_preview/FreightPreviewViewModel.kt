package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.FreightUseCase
import kotlinx.coroutines.launch

class FreightPreviewViewModel(
    private val vmData: FreightPreviewVmData,
    private val repository: FreightRepository,
    private val customerRepository: CustomerRepository,
    private val useCase: FreightUseCase
) : ViewModel() {

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

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            useCase.getFreightByIdFlow(vmData.freightId) { response ->
                _data.value = when (response) {
                    is Response.Error -> response
                    is Response.Success -> {
                        response.data?.let { freight ->
                            _writeAuth.value = !freight.isValid
                            Response.Success(freight)
                        } ?: Response.Error(NullPointerException())
                    }
                }
            }
        }
    }

    fun delete() = liveData<Response<Unit>>(viewModelScope.coroutineContext) {
        try {
            val writeReq = WriteRequest(
                authLevel = vmData.permission,
                data = (data.value as Response.Success).data!!.toDto()
            )
            useCase.delete(writeReq)
            //useCase.delete(vmData.permission, dto)
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

}

data class FreightPreviewVmData(
    val freightId: String,
    val permission: PermissionLevelType
)