package br.com.apps.trucktech.ui.fragments.nav_home.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.TravelUseCase

class PaymentViewModel(private val travelUseCase: TravelUseCase) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init { setState(State.Loading) }

    fun setState(state: State) {
        if (this@PaymentViewModel.state.value != state) {
            _state.value = state
        }
    }

    fun updateData(travelList: List<Travel>): List<Freight> {
        val freights = travelUseCase.getFreightListWitchIsNotPaidYet(travelList)
        fun getState(): State {
            return if (freights.isEmpty()) State.Empty
            else State.Loaded
        }
        setState(getState())
        return freights
    }

}