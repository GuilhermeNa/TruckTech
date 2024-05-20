package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.freight.FreightRepository
import kotlinx.coroutines.launch

class FreightPreviewViewModel(
    private val freightId: String,
    private val repository: FreightRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Freight]
     * to be displayed on screen.
     */
    private val _freightData = MutableLiveData<Response<Freight>>()
    val freightData get() = _freightData

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
            repository.getFreightById(freightId, true).asFlow().collect {
                _freightData.value = it
            }
        }
    }

    fun delete() = liveData<Response<Unit>>(viewModelScope.coroutineContext) {
        try {
            repository.delete(freightId)
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