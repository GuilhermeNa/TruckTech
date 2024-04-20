package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Freight
import br.com.apps.usecase.TravelUseCase
import kotlinx.coroutines.launch

class FreightsListFragmentViewModel(
    private val travelId: String,
    private val useCase: TravelUseCase
): ViewModel() {

    private val _freightData = MutableLiveData<Freight>()
    val freightData get() = _freightData

    init {
        loadData()
        Log.d("teste", travelId)
    }

    fun loadData() {
        viewModelScope.launch {
            //TODO estou buscando a lista de fretes para este ID, além disso,
            // preciso estruturar uma forma de desenhar meu repositorio, use case e etc
        }
    }

}