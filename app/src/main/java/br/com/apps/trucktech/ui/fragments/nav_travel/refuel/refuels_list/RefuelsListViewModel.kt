package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuels_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.RefuelRepository
import kotlinx.coroutines.launch

class RefuelsListViewModel(
    private val travelId: String,
    private val repository: RefuelRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [Refuel]
     * to be displayed on screen.
     */
    private val _refuelData = MutableLiveData<Response<List<Refuel>>>()
    val refuelData get() = _refuelData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getRefuelListByTravelId(travelId, true).asFlow().collect {
                _refuelData.value = it
            }
        }
    }

}