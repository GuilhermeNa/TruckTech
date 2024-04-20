package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.Response
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

}