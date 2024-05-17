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
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.RequestRepository
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
    fun save(mappedFields: HashMap<String, String>) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = getItemDto(mappedFields)
                repository.saveItem(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private fun getItemDto(mappedFields: HashMap<String, String>): RequestItemDto {
        return if (::requestItem.isInitialized) {
            RequestItemFactory.update(requestItem, mappedFields)
            requestItem.toDto()
        } else {
            mappedFields[RequestItemFactory.TAG_REQUEST_ID] = requestId
            RequestItemFactory.create(mappedFields, RequestItemType.REFUEL).toDto()
        }
    }

}
