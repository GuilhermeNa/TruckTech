package br.com.apps.trucktech.ui.fragments.nav_home.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.FreightRepository
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val driverId: String,
    private val repository: FreightRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Freight]
     * to be displayed on screen.
     */
    private val _freightData = MutableLiveData<Response<List<Freight>>>()
    val freightData get() = _freightData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getFreightListByDriverIdAndPaymentStatus(
                driverId,
                isPaid = false,
                withFlow = false
            ).asFlow().collect {
                _freightData.value = it
            }
        }
    }

}