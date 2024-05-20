package br.com.apps.trucktech.ui.fragments.nav_home.discount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.repository.repository.AdvanceRepository
import br.com.apps.repository.repository.LoanRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.launch

class DiscountViewModel(
    private val employeeId: String,
    private val loanRepository: LoanRepository,
    private val advanceRepository: AdvanceRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Advance]
     * to be displayed on screen.
     */
    private val _advanceData = MutableLiveData<Response<List<Advance>>>()
    val advanceData get() = _advanceData

    /**
     * LiveData holding the response data of type [Response] with a [Loan]
     * to be displayed on screen.
     */
    private val _loanData = MutableLiveData<Response<List<Loan>>>()
    val loanData get() = _loanData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            launch { loadAdvanceData() }
            launch { loadLoanData() }
        }
    }

    private suspend fun loadAdvanceData() {
        advanceRepository.getAdvanceListByEmployeeIdAndPaymentStatus(employeeId, isPaid = false)
            .asFlow().collect {
                _advanceData.value = it
            }
    }

    private suspend fun loadLoanData() {
        loanRepository.getLoanListByEmployeeIdAndPaymentStatus(employeeId, isPaid = false)
            .asFlow().collect {
                _loanData.value = it
            }
    }


}