package br.com.apps.trucktech.ui.fragments.nav_travel.records

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecordsViewModel(
    private val repository: TravelRepository
) : ViewModel() {

    lateinit var travelId: String
    lateinit var masterUid: String

    private val _state = MutableLiveData<StateR>()
    val state get() = _state

    private var _viewPagerPosition = MutableLiveData(0)
    val viewPagerPosition get() = _viewPagerPosition

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    fun newPageHasBeenSelected(position: Int) {
        _viewPagerPosition.value = position
    }

    fun loadData() {
        viewModelScope.launch {
            val response = repository.getTravelById(travelId).asFlow().first()
            val state = when (response) {
                is Response.Error -> StateR.Error(response.exception)
                is Response.Success -> {
                    val data = response.data
                    when {
                        data == null -> StateR.Error(NullPointerException())
                        data.isFinished -> StateR.LoadedR.IsFinished
                        else -> StateR.LoadedR.IsUnfinished
                    }
                }
            }
            setState(state)
        }
    }

    private fun setState(state: StateR) {
        if (_state.value != state) _state.value = state
    }

}

sealed class StateR {
    sealed class LoadedR : StateR() {
        object IsFinished : LoadedR()
        object IsUnfinished : LoadedR()
    }
    data class Error(val error: Exception) : StateR()
}