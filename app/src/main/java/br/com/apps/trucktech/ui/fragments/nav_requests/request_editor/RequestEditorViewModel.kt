package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.RequestDto
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.null_objects.NullItemException
import br.com.apps.model.exceptions.null_objects.NullRequestException
import br.com.apps.model.model.request.Item
import br.com.apps.model.model.request.Request
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.usecase.RequestUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RequestEditorViewModel(
    private val vmData: RequestEditorVmData,
    private val requestRepo: RequestRepository,
    private val itemRepo: ItemRepository,
    private val useCase: RequestUseCase,
    private val storage: StorageRepository
) : ViewModel() {

    private val _urlImage = MutableLiveData<Any>()
    val urlImage get() = _urlImage

    /**
     * LiveData holding the response data of type [Response] with a [PaymentRequest]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<Request>>()
    val data get() = _data

    /**
     * LiveData with a dark layer state, used when dialogs and bottom sheets are requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        /* loadData { request, items ->
             request.addAll(items)
             sendResponse(request)
         }*/
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {

            combine(
                requestRepo.fetchRequestById(vmData.requestId, true).asFlow(),
                itemRepo.fetchItemsByParentId(vmData.requestId, true).asFlow()
            ) { requestResponse, itemResponse ->
                val request = when (requestResponse) {
                    is Response.Error -> throw requestResponse.exception
                    is Response.Success -> requestResponse.data ?: throw NullRequestException()
                }
                val items = when (itemResponse) {
                    is Response.Error -> throw itemResponse.exception
                    is Response.Success -> itemResponse.data ?: throw NullItemException()
                }

                request.addAll(items)
                sendResponse(request)

            }
        }
    }

    private fun sendResponse(request: Request) {
        if (_urlImage.value == null) {
            request.urlImage?.let { _urlImage.value = it }
        }
        _data.value = Response.Success(request)
    }

    fun deleteItem(item: Item) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            /*      try {
                      val dto = getRequestDto()
                      useCase.deleteItem(vmData.permission, dto, item)
                      emit(Response.Success())

                  } catch (e: Exception) {
                      e.printStackTrace()
                      emit(Response.Error(e))
                  }*/
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

    fun imageHaveBeenLoaded(image: ByteArray) {
        _urlImage.value = image
        saveEncodedImage()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveEncodedImage() {
        val dto = getRequestDto()
        val ba = _urlImage.value as ByteArray

        GlobalScope.launch {
            val url = storage.postImage(ba, "requests/${dto.id}.jpeg")
            requestRepo.updateUrlImage(dto.id!!, url)
        }

    }

    private fun getRequestDto(): RequestDto {
        return (data.value as Response.Success).data!!.toDto()
    }

}

data class RequestEditorVmData(
    val requestId: String,
    val permission: AccessLevel
)