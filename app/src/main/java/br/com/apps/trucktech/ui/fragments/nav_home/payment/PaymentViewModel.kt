package br.com.apps.trucktech.ui.fragments.nav_home.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.buildUiResponse
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val driverId: String,
    private val repository: FreightRepository
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state get() = _state

    /**
     * LiveData holding the response data of type [Response] with a [Freight]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Freight>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        _state.value = State.Loading
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getFreightListByDriverIdAndIsNotPaidYet(driverId)
                .asFlow().first {
                    it.buildUiResponse(_state, _data)
                    true
                }
        }
    }

}