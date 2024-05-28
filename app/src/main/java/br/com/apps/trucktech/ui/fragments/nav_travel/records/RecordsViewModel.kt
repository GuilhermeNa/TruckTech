package br.com.apps.trucktech.ui.fragments.nav_travel.records

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecordsViewModel(
    private val repository: TravelRepository
): ViewModel() {

    lateinit var travelId: String
    lateinit var masterUid: String

    private var _travel = MutableLiveData<Response<Travel>>()
    val travel get() = _travel

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
            repository.getTravelById(travelId).asFlow().first {
                _travel.value = it
                true
            }
        }
    }

}