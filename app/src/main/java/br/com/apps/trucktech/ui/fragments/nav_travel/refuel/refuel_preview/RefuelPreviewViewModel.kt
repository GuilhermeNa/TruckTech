package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.RefuelUseCase
import kotlinx.coroutines.launch

class RefuelPreviewViewModel(
    private val vmData: RefuelPreviewVmData,
    private val repository: RefuelRepository,
    private val useCase: RefuelUseCase
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Refuel]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<Refuel>>()
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
            repository.getRefuelById(vmData.refuelId, true).asFlow().collect { response ->
                _data.value = when (response) {
                    is Response.Error -> response
                    is Response.Success -> {
                        response.data?.let { refuel ->
                            _writeAuth.value = !refuel.isValid
                            Response.Success(refuel)
                        } ?: Response.Error(NullPointerException())
                    }
                }
            }
        }
    }

    fun delete() =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val writeReq = WriteRequest(
                    authLevel = vmData.permission,
                    data = (_data.value as Response.Success).data!!.toDto()
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

data class RefuelPreviewVmData(
    val refuelId: String,
    val permission: PermissionLevelType
)