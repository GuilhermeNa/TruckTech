package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuels_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.exceptions.null_objects.NullRefuelException
import br.com.apps.model.model.travel.Refuel
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.trucktech.util.state.State
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

    init {
        setFragmentState(State.Loading)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                loadRefuels(::sendResponse)

            } catch (e: Exception) {
                e.printStackTrace()
                setFragmentState(State.Error(e))

            }
        }
    }

    private suspend fun loadRefuels(complete: (refuels: List<Refuel>) -> Unit) {
        repository.fetchRefuelListByTravelId(travelId, true).asFlow().collect { response ->
            when(response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data?.let { complete(it) }
                    ?: throw NullRefuelException(UNKNOWN_EXCEPTION)
            }
        }
    }

    private fun sendResponse(data: List<Refuel>) {
        if(data.isEmpty()) setFragmentState(State.Empty)
        else {
            setFragmentData(data)
            setFragmentState(State.Loaded)
        }
    }

    private fun setFragmentState(state: State) {
        if(state != _state.value) _state.value
    }

    private fun setFragmentData(refuels: List<Refuel>) {
        _data.value = refuels
    }

}