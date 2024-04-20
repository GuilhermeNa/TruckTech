package br.com.apps.trucktech.ui.fragments.nav_home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.Fine
import br.com.apps.usecase.FineUseCase
import kotlinx.coroutines.launch

class HomeFragmentFineViewModel(private val useCase: FineUseCase): ViewModel() {

    private val _fineDataSet = MutableLiveData<List<Fine>>()
    val fineDataSet get() = _fineDataSet


    fun loadFines(driverId: String) {
        viewModelScope.launch {
            useCase.getAllByDriverId(driverId).asFlow().collect {
                _fineDataSet.value = it
            }
        }
    }


}