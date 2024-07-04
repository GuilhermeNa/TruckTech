package br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.ExpendUseCase
import kotlinx.coroutines.launch

class  ExpendPreviewViewModel(
    private val vmData: ExpendPreviewVmData,
    private val repository: ExpendRepository,
    private val useCase: ExpendUseCase
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Expend]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<Expend>>()
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

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getExpendById(vmData.expendId, true)
                .asFlow().collect { response ->
                    _data.value = when (response) {
                        is Response.Error -> response
                        is Response.Success -> {
                            response.data?.let { expend ->
                                _writeAuth.value = !expend.isValid
                                Response.Success(expend)
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

data class ExpendPreviewVmData(
    val expendId: String,
    val permission: PermissionLevelType
)