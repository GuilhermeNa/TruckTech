package br.com.apps.trucktech.ui.fragments.nav_travel.records

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.util.Response
import br.com.apps.usecase.TravelUseCase
import kotlinx.coroutines.launch

class RecordsViewModel(private val useCase: TravelUseCase): ViewModel() {

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
            useCase.getTravelById(travelId).asFlow().collect {
                _travel.value = it
            }
        }
    }



}