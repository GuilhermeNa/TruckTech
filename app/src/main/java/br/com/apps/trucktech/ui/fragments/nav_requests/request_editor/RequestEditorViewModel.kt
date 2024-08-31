package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.null_objects.NullItemException
import br.com.apps.model.exceptions.null_objects.NullRequestException
import br.com.apps.model.model.request.Item
import br.com.apps.model.model.request.Request
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.trucktech.util.SingleLiveEvent
import br.com.apps.trucktech.util.image.Image
import br.com.apps.trucktech.util.state.DataEvent
import br.com.apps.usecase.usecase.ItemUseCase
import br.com.apps.usecase.util.buildRequestStoragePath
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RequestEditorViewModel(
    private val vmData: RequestEditorVmData,
    private val requestRepo: RequestRepository,
    private val itemRepo: ItemRepository,
    private val itemUseCase: ItemUseCase,
    private val storage: StorageRepository
) : ViewModel() {

    private var isFirstBoot = true

    private val _image = MutableLiveData<Image?>()
    val image get() = _image

    private lateinit var _request: Request
    val request get() = if (::_request.isInitialized) _request else null

    /**
     * LiveData holding the response data of type [Response] with a [DataEvent]
     * to be displayed on screen.
     */
    private val _dataEvent = SingleLiveEvent<DataEvent<List<Item>>>()
    val dataEvent get() = _dataEvent

    /**
     * LiveData with a dark layer state, used when dialogs and bottom sheets are requested.
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
            try {
                _request = loadRequest()

                loadItemsFlow { newItems ->
                    val oldItems = request!!.items.toList()
                    _request.addAll(newItems)
                    setNewDataEvent(getDataEvent(oldItems = oldItems, newItems = newItems))
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun loadRequest(): Request {
        val response = requestRepo.fetchRequestById(vmData.requestId, true)
            .asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data?.apply {
                urlImage?.let { url -> setImage(url) }
            } ?: throw NullRequestException()
        }
    }

    private suspend fun loadItemsFlow(complete: (List<Item>) -> Unit) {
        itemRepo.fetchItemsByParentIdAndDateDesc(vmData.requestId, true).asFlow()
            .collect { response ->
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> complete(response.data ?: throw NullItemException())
                }
            }
    }

    private fun getDataEvent(oldItems: List<Item>, newItems: List<Item>): DataEvent<List<Item>> {
        return if (isFirstBoot) {
            isFirstBoot = false
            DataEvent.Initialize(data = newItems)

        } else {
            val addedItems = newItems.filter { newItem ->
                newItem.id !in oldItems.map { oldItems -> oldItems.id }
            }
            val removedItems = oldItems.filter {
                it.id !in newItems.map { newItem -> newItem.id }
            }
            val editedItems = newItems.filter { newItem ->
                oldItems.find { it.id == newItem.id }
                    ?.let { oldItem -> oldItem != newItem } == true
            }

            when {
                addedItems.isNotEmpty() -> DataEvent.Insert(item = addedItems)
                removedItems.isNotEmpty() -> DataEvent.Remove(item = removedItems)
                else -> DataEvent.Update(item = editedItems)
            }
        }

    }

    private fun setNewDataEvent(newDataEvent: DataEvent<List<Item>>) {
        _dataEvent.value = newDataEvent
    }

    fun deleteItem(itemId: String) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val writeReq = WriteRequest(
                    authLevel = vmData.permission,
                    data = _request.getItemById(itemId).toDto()
                )
                itemUseCase.delete(writeReq)
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

    fun imageHaveBeenLoaded(ba: ByteArray) {
        setImage(ba)
        saveEncodedImage()
    }

    private fun setImage(image: Any) {
        _image.value = when(image) {
            is String -> Image(url = image)
            else-> Image(byteArray = image as ByteArray)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveEncodedImage() {
        val dto = _request.toDto()
        val ba = _image.value!!.byteArray!!

        GlobalScope.launch {
            requestRepo.setUpdatingStatus(dto.id!!, true)
            val url = storage.postImage(ba, buildRequestStoragePath(dto.id!!))
            requestRepo.updateUrlImage(dto.id!!, url)
        }

    }

    fun getItems(): List<Item> {
        return if (::_request.isInitialized) _request.items
        else emptyList()
    }

}

data class RequestEditorVmData(
    val requestId: String,
    val permission: AccessLevel
)

