package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_wallet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.factory.RequestItemFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.request.travel_requests.RequestItemType
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.usecase.RequestUseCase
import kotlinx.coroutines.launch

class RequestEditorWalletFragmentViewModel(
    private val vmData: RequestEditorWalletVmData,
    private val repository: RequestRepository,
    private val useCase: RequestUseCase
) : ViewModel() {

    private var isEditing: Boolean = vmData.walletId?.let { true } ?: false

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [RequestItem]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<RequestItem>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        vmData.walletId?.let { loadData(it) }
    }

    /**
     * Load data
     */
    fun loadData(walletId: String) {
        viewModelScope.launch {
            repository.getItemById(vmData.requestId, walletId)
                .asFlow().collect {
                    _data.value = it
                }
        }
    }

    /**
     * Transforms the mapped fields into a DTO. Creates a new [RequestItem] or updates it if it already exists.
     */
    fun save(viewDto: RequestItemDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = createOrUpdate(viewDto)
                useCase.saveItem(vmData.permission, dto)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

    private fun createOrUpdate(viewDto: RequestItemDto): RequestItemDto {
        return when (isEditing) {
            true -> {
                val item = (data.value as Response.Success).data!!
                RequestItemFactory.update(item, viewDto)
                item.toDto()
            }

            false -> {
                viewDto.requestId = vmData.requestId
                viewDto.type = RequestItemType.WALLET.description
                RequestItemFactory.create(viewDto, RequestItemType.WALLET).toDto()
            }
        }
    }

}

data class RequestEditorWalletVmData(
    val requestId: String,
    val walletId: String? = null,
    val permission: PermissionLevelType
)