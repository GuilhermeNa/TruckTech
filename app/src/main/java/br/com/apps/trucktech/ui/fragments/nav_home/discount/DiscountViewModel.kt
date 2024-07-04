package br.com.apps.trucktech.ui.fragments.nav_home.discount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.repository.advance.AdvanceRepository
import br.com.apps.repository.repository.travel_aid.TravelAidRepository
import br.com.apps.repository.repository.loan.LoanRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DiscountViewModel(
    private val employeeId: String,
    private val loanRepository: LoanRepository,
    private val advanceRepository: AdvanceRepository,
    private val costHelpRepository: TravelAidRepository
) : ViewModel() {

    private val _state = MutableLiveData<DiscountFState>()
    val state get() = _state

    /**
     * LiveData holding the response data of type [Response] with a [Advance]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<DiscountFData>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(DiscountFState.Loading)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val costHelps = loadCostHelpData()
                val advances = loadAdvanceData()
                val loans = loadLoanData()

                val state =
                    if (advances.isNotEmpty() || loans.isNotEmpty() || costHelps.isNotEmpty()) {
                    DiscountFState.Loaded(
                        hasCostHelps = costHelps.isNotEmpty(),
                        hasAdvances = advances.isNotEmpty(),
                        hasLoans = loans.isNotEmpty()
                    )
                } else {
                    DiscountFState.Empty
                }

                setState(state)

                _data.postValue(
                    DiscountFData(
                        costHelps = costHelps,
                        advances = advances,
                        loans = loans
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
                setState(DiscountFState.Error(e))

            }

        }
    }

    private suspend fun loadCostHelpData(): List<TravelAid> {
        val response =
            costHelpRepository.getTravelAidByDriverIdAndIsNotDiscountedYet(employeeId)
                .asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

    private suspend fun loadAdvanceData(): List<Advance> {
        val response =
            advanceRepository.getAdvanceListByEmployeeIdAndPaymentStatus(employeeId, isPaid = false)
                .asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

    private suspend fun loadLoanData(): List<Loan> {
        val response =
            loanRepository.getLoanListByEmployeeIdAndPaymentStatus(employeeId, isPaid = false)
                .asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

    fun setState(state: DiscountFState) {
        _state.value = state
    }

}

data class DiscountFData(
    val costHelps: List<TravelAid>,
    val advances: List<Advance>,
    val loans: List<Loan>
)

