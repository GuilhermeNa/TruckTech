package br.com.apps.trucktech.ui.fragments.nav_requests.item_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.null_objects.NullItemException
import br.com.apps.model.model.request.Item
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.ItemUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ItemEditorViewModel(
    private val vmData: ItemEditorVmData,
    private val useCase: ItemUseCase,
    private val repository: ItemRepository
) : ViewModel() {

    private val isEditing = vmData.itemId?.let { true } ?: false

    private var _isFirstBoot = true
    val isFirstBoot get() = _isFirstBoot

    private var _state = MutableLiveData<State>()
    val state get() = _state

    private var _data = MutableLiveData<Item>()
    val data get() = _data

    private var _urlImage = MutableLiveData<Any?>()
    val urlImage get() = _urlImage

    private var _viewHeight = 0
    val viewHeight get() = _viewHeight

    private fun setFirstBoot() {
        if (_isFirstBoot) _isFirstBoot = false
    }

    private fun setState(newState: State) {
        if (_state != newState) _state.value = newState
    }

    private fun setData(data: Item) {
        _data.value = data
    }

    fun setUrlImage(bytes: ByteArray?) {
        _urlImage.value = bytes
    }

    fun setViewHeight(height: Int) {
        if (_viewHeight == 0) _viewHeight = height

    }

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

}

class ItemEditorVmData(
    val requestId: String,
    val itemId: String?,
    val access: AccessLevel
)