package br.com.apps.trucktech.ui.fragments.nav_home.refund

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.travel.Expend
import br.com.apps.repository.Response
import br.com.apps.repository.repository.ExpendRepository
import kotlinx.coroutines.launch

class RefundViewModel(
    private val driverId: String,
    private val repository: ExpendRepository
): ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Expend]
     * to be displayed on screen.
     */
    private val _expendData = MutableLiveData<Response<List<Expend>>>()
    val expendData get() = _expendData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
           repository.getExpendListByDriverIdAndRefundableStatus(
               driverId = driverId,
               paidByEmployee = true,
               alreadyRefunded = false,
               withFlow = false
           ).asFlow().collect {
            _expendData.value = it
           }
        }
    }


}