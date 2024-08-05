package br.com.apps.trucktech.ui.fragments.nav_home.refund

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.Travel
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.TravelUseCase

class RefundViewModel(private val travelUseCase: TravelUseCase) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init { setState(State.Loading) }

    fun setState(state: State) {
        _state.value = state
    }

    fun updateData(travelList: List<Travel>): List<Outlay> {
        val expends = travelUseCase.getExpendListWitchIsNotRefundYet(travelList)
        fun getState(): State {
            return if (expends.isEmpty()) State.Empty
            else State.Loaded
        }
        setState(getState())
        return expends
    }


}