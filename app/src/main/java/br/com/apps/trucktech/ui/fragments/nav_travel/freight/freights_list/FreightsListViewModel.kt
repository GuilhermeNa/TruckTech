package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.FreightRepository
import kotlinx.coroutines.launch

class FreightsListViewModel(
    private val travelId: String,
    private val repository: FreightRepository,
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [Freight]
     * to be displayed on screen.
     */
    private val _freightData = MutableLiveData<Response<List<Freight>>>()
    val freightData get() = _freightData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getFreightListByTravelId(travelId, true).asFlow().collect {
                _freightData.value = it
            }
        }
    }

}