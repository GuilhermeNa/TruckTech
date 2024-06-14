package br.com.apps.trucktech.ui.fragments.nav_home.fines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.model.Fine
import br.com.apps.repository.repository.fine.FineRepository
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.buildUiResponse
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.launch

class FinesListViewModel(
    private val idHolder: IdHolder,
    private val repository: FineRepository
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state get() = _state

    /**
     * LiveData holding the response data of type [Response] with a list of [Fine]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Fine>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
        loadData()
    }

    private fun loadData() {
        val id = idHolder.driverId ?: throw IllegalArgumentException(EMPTY_ID)
        viewModelScope.launch {
            repository.getFineListByDriverId(id, false).asFlow().collect {
                it.buildUiResponse(_state, _data)
            }
        }
    }

    fun setState(state: State) {
        _state.value = state
    }

}