package br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.launch

class ExpendPreviewViewModel(
    private val expendId: String,
    private val repository: ExpendRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Expend]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<Expend>>()
    val data get() = _data

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
            repository.getExpendById(expendId, true).asFlow().collect {
                _data.value = it
            }
        }
    }

    fun delete() = liveData<Response<Unit>>(viewModelScope.coroutineContext) {
        try {
            repository.delete(expendId)
            emit(Response.Success())
        } catch (e: Exception) {
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