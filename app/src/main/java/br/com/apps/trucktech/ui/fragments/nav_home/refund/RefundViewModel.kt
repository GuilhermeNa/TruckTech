package br.com.apps.trucktech.ui.fragments.nav_home.refund

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.buildUiResponse
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RefundViewModel(
    private val driverId: String,
    private val repository: ExpendRepository
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state get() = _state

    /**
     * LiveData holding the response data of type [Response] with a [Expend]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Expend>>()
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
            repository.getExpendListByDriverIdAndIsNotRefundYet(driverId)
                .asFlow().first {
                    it.buildUiResponse(_state, _data)
                    true
                }
        }
    }

}