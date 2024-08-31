package br.com.apps.trucktech.ui.fragments.nav_requests.item_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.ItemDto
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.null_objects.NullItemException
import br.com.apps.model.model.request.Item
import br.com.apps.model.util.toDate
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.trucktech.util.SingleLiveEvent
import br.com.apps.trucktech.util.image.Image
import br.com.apps.trucktech.util.image.ImageUseCase
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.ItemUseCase
import br.com.apps.usecase.util.buildRequestStoragePath
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(DelicateCoroutinesApi::class)
class ItemEditorViewModel(
    private val vmData: ItemEditorVmData,
    private val useCase: ItemUseCase,
    private val repository: ItemRepository,
    private val storage: StorageRepository
) : ViewModel() {

    private val isEditing = vmData.itemId?.let { true } ?: false

    private var _isFirstBoot = true
    val isFirstBoot get() = _isFirstBoot

    private var _state = MutableLiveData<State>()
    val state get() = _state

    private var _data = SingleLiveEvent<Item>()
    val data get() = _data

    private var _image = MutableLiveData<Image?>()
    val image get() = _image

    private var _viewHeight = 0
    val viewHeight get() = _viewHeight

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(300)
            when (isEditing) {
                true -> {
                    try {
                        repository.fetchItemById(vmData.itemId!!).asFlow().first { response ->
                            when (response) {
                                is Response.Error -> throw response.exception
                                is Response.Success -> response.data?.run {
                                    setState(State.Loaded)
                                    this.urlImage?.let { setUrlImage(it) }
                                    setData(this)
                                } ?: throw NullItemException()
                            }
                            true
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        setState(State.Error(e))
                    }
                }

                false -> setState(State.Loaded)
            }
            setFirstBoot()
        }
    }

    fun save(viewDto: ItemDto) = liveData(viewModelScope.coroutineContext) {
        try {
            val writeReq = WriteRequest(
                authLevel = vmData.accessLevel,
                data = generateDto(viewDto)
            )

            ImageUseCase.checkDirection(
                isEditing = isEditing,
                image = image.value,
                hasPreviousImg = _data.value?.urlImage != null
            ).let { dir ->
                when (dir) {
                    ImageUseCase.ADDING_WITH_IMAGE -> {
                        writeReq.data.isUpdating = true
                        val id = useCase.save(writeReq)
                        GlobalScope.launch {
                            val url =
                                storage.postImage(image.value!!.byteArray!!, buildRequestStoragePath(id))
                            repository.updateUrl(id, url)
                        }
                    }

                    ImageUseCase.ADDING_WITHOUT_IMAGE -> {
                        writeReq.data.isUpdating = false
                        useCase.save(writeReq)
                    }

                    ImageUseCase.EDITING_AND_REPLACING_IMAGE -> {
                        writeReq.data.isUpdating = true
                        val id = useCase.save(writeReq)
                        GlobalScope.launch {
                            val url =
                                storage.postImage(image.value!!.byteArray!!, buildRequestStoragePath(id))
                            repository.updateUrl(id, url)
                        }
                    }

                    ImageUseCase.EDITING_WITHOUT_NEW_IMAGE -> {
                        writeReq.data.isUpdating = false
                        useCase.save(writeReq)
                    }

                    ImageUseCase.EDITING_AND_REMOVING_IMAGE -> {
                        writeReq.data.isUpdating = false
                        writeReq.data.urlImage = null
                        val idToDelete = useCase.save(writeReq)
                        GlobalScope.launch {
                            storage.deleteImage(buildRequestStoragePath(idToDelete))
                        }
                    }

                    ImageUseCase.EDITING_AND_INSERTING_FIRST_IMAGE -> {
                        writeReq.data.isUpdating = true
                        val id = useCase.save(writeReq)
                        GlobalScope.launch {
                            val url =
                                storage.postImage(image.value!!.byteArray!!, buildRequestStoragePath(id))
                            repository.updateUrl(id, url)
                        }
                    }

                    else -> throw NullPointerException()
                }
            }

            emit(Response.Success())

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error(e))
        }
    }

    private fun generateDto(viewDto: ItemDto): ItemDto {
        fun setCommonFields() {
            viewDto.run {
                masterUid = vmData.masterUid
                parentId = vmData.requestId
            }
        }

        fun whenEditing(): ItemDto {
            val item = data.value!!
            setCommonFields()
            return viewDto.apply {
                id = item.id
                isValid = item.isValid
                urlImage = item.urlImage
                date = item.date.toDate()
            }
        }

        fun whenCreating(): ItemDto {
            setCommonFields()
            return viewDto.apply {
                isValid = false
                date = LocalDateTime.now().toDate()
            }
        }

        return if (isEditing) whenEditing()
        else whenCreating()
    }

    private fun setFirstBoot() {
        if (_isFirstBoot) _isFirstBoot = false
    }

    private fun setState(newState: State) {
        if (_state != newState) _state.value = newState
    }

    private fun setData(data: Item) {
        _data.value = data
    }

    private fun setUrlImage(urlImage: String?) {
        _image.value = Image(url = urlImage)
    }

    fun setBaImage(ba: ByteArray?) {
        _image.value = Image(byteArray = ba)
    }

    fun setViewHeight(height: Int) {
        if (_viewHeight == 0) _viewHeight = height
    }

    fun userDeleteTheImage() {
        _image.value = null
    }

    fun getImageData() = image.value?.getData()

}

class ItemEditorVmData(
    val masterUid: String,
    val requestId: String,
    val itemId: String?,
    val accessLevel: AccessLevel
)

