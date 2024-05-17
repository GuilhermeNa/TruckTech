package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.RefuelRepository
import kotlinx.coroutines.launch

class RefuelPreviewViewModel(
    private val refuelId: String,
    private val repository: RefuelRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Refuel]
     * to be displayed on screen.
     */
    private val _refuelData = MutableLiveData<Response<Refuel>>()
    val refuelData get() = _refuelData

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
            repository.getRefuelById(refuelId, true).asFlow().collect {
                _refuelData.value = it
            }
        }
    }

    fun delete() =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                repository.delete(refuelId)
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