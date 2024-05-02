package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.Response
import br.com.apps.repository.repository.FreightRepository
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

}