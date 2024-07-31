package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel

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
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.usecase.RequestUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RequestEditorRefuelViewModel(
    private val vmData: RequestEditorRefuelVmData,
    private val repository: RequestRepository,
    private val useCase: RequestUseCase,
    private val storage: StorageRepository
) : ViewModel() {

    private var isEditing: Boolean = vmData.refuelReqId?.let { true } ?: false

    private val _loadedImage = MutableLiveData<Any?>()
    val loadedImage get() = _loadedImage

    /**
     * LiveData holding the response data of type [Response] with a list of [RequestItem]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<RequestItem>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        vmData.refuelReqId?.let { loadData(it) }
    }

    /**
     * Load data
     */
    fun loadData(itemId: String) {
        viewModelScope.launch {
            val response =
                repository.fetchItemById(requestId = vmData.requestId, itemId = itemId).asFlow()
                    .first()

            _data.value = when (response) {
                is Response.Error -> Response.Error(response.exception)
                is Response.Success -> {
                    response.data?.let {
                        it.docUrl?.let { doc -> _loadedImage.value = doc }
                        Response.Success(it)
                    } ?: Response.Error(NullPointerException())
                }
            }

        }
    }

    /**
     * Transforms the mapped fields into a DTO. Creates a new [RequestItem] or updates it if it already exists.
     */
    fun save(viewDto: RequestItemDto, ba: ByteArray?) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = createOrUpdate(viewDto)
                val id = useCase.saveItem(vmData.permission, dto)
                dto.id = id
                emit(Response.Success())

                loadedImage.value?.let {
                    saveImage(ba!!, dto)
                }


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
                if (loadedImage.value == null) item.docUrl = null
                item.toDto()
            }

            false -> {
                viewDto.requestId = vmData.requestId
                viewDto.type = RequestItemType.REFUEL.description
                RequestItemFactory.create(viewDto, RequestItemType.REFUEL).toDto()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun saveImage(ba: ByteArray, dto: RequestItemDto) {
        GlobalScope.launch {
            try {
                val url = storage.postImage(ba, "requests/${dto.id}.jpeg")
                repository.updateItemImageUrl(dto, url)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun updateImage(urlImage: ByteArray) {
        _loadedImage.value = urlImage
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteImage() {
        _loadedImage.value = null
        val id = (data.value as Response.Success).data?.id
        GlobalScope.launch {
            try {
                id?.let { storage.deleteImage("requests/$id.jpg") }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}

data class RequestEditorRefuelVmData(
    val requestId: String,
    val refuelReqId: String? = null,
    val permission: PermissionLevelType
)