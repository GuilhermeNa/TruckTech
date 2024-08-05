package br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.exceptions.null_objects.NullExpendException
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.user.AccessLevel
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.ExpendUseCase
import kotlinx.coroutines.launch

class  ExpendPreviewViewModel(
    private val vmData: ExpendPreviewVmData,
    private val repository: OutlayRepository,
    private val useCase: ExpendUseCase
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Outlay]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<Outlay>>()
    val data get() = _data

    private val _writeAuth = MutableLiveData<Boolean>()
    val writeAuth get() = _writeAuth

    /**
     * LiveData with a dark layer state, used when dialog is requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            try {
                loadExpendFlow(::sendResponse)

            } catch (e: Exception) {
                setFragmentData(Response.Error(e))

            }
        }
    }

    private suspend fun loadExpendFlow(complete: (expends: Outlay) -> Unit) {
        repository.fetchOutlayById(vmData.expendId, true).asFlow().collect { response ->
            when(response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data?.let { complete(it) }
                    ?: throw NullExpendException(UNKNOWN_EXCEPTION)
            }
        }
    }

    private fun sendResponse(outlay: Outlay) {
        setWriteAuth(!outlay.isValid)
        setFragmentData(Response.Success(data = outlay))
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

    private fun setFragmentData(response: Response<Outlay>) {
        _data.value = response
    }

}

data class ExpendPreviewVmData(
    val expendId: String,
    val permission: AccessLevel
)