package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.factory.RequestItemFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.model.model.request.request.RequestItemType
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.launch

class RequestEditorRefuelViewModel(
    idHolder: IdHolder,
    private val repository: RequestRepository
) : ViewModel() {

    val requestId = idHolder.requestId ?: throw NullPointerException("Null requestId")

    /**
     * Holds a [RequestItem] when the data is loaded, to be saved when necessary.
     */
    lateinit var requestItem: RequestItem

    /**
     * LiveData holding the response data of type [Response] with a list of [RequestItem]
     * to be displayed on screen.
     */
    private val _refuelData = MutableLiveData<Response<RequestItem>>()
    val refuelData get() = _refuelData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        idHolder.refuelId?.let { loadData(it) }
    }

    /**
     * Load data
     */
    fun loadData(itemId: String) {
        viewModelScope.launch {
            val liveData = repository.getItemById(requestId = requestId, itemId = itemId)

            liveData.asFlow().collect { response ->
                when (response) {
                    is Response.Error -> _refuelData.value = response

                    is Response.Success -> {
                        response.data?.let { requestItem = it }
                        _refuelData.value = response
                    }
                }
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
                repository.saveItem(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private fun createOrUpdate(viewDto: RequestItemDto): RequestItemDto {
        return if (::requestItem.isInitialized) {
            RequestItemFactory.update(requestItem, viewDto)
            requestItem.toDto()
        } else {
            viewDto.requestId = this@RequestEditorRefuelViewModel.requestId
            RequestItemFactory.create(viewDto, RequestItemType.REFUEL).toDto()
        }
    }

}
