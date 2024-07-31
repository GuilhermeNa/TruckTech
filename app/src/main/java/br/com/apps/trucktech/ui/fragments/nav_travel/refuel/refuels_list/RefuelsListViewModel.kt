package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuels_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.state.State
import br.com.apps.trucktech.util.buildUiResponse
import kotlinx.coroutines.launch

class RefuelsListViewModel(
    private val travelId: String,
    private val repository: RefuelRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [Refuel]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Refuel>>()
    val data get() = _data

    private val _state = MutableLiveData<State>()
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init { loadData() }

    private fun loadData() {
        _state.value = State.Loading
        viewModelScope.launch {
            repository.fetchRefuelListByTravelId(travelId, true).asFlow().collect { response ->
                response.buildUiResponse(state = _state, data = _data)
            }
        }
    }

}