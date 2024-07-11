package br.com.apps.trucktech.ui.fragments.nav_home.fines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.trucktech.util.state.State

class FinesListViewModel: ViewModel() {

    private val _state = MutableLiveData<State>()
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
    }

    fun setState(state: State) {
        _state.value = state
    }

}

