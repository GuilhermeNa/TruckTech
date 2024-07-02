package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.exceptions.NoBanksFoundException
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BankListFragmentViewModel(
    private val employeeId: String,
    private val employeeRepository: EmployeeRepository,
    private val bankRepository: BankRepository
) : ViewModel() {

    private lateinit var banks: List<Bank>

    private var _adapterPos = -1
    val adapterClickedPos get() = _adapterPos

    private val _data = MutableLiveData<BankLFData>()
    val data get() = _data

    private val _state = MutableLiveData<State>(State.Loading)
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(1000)

            try {
                banks = loadBanks()
                loadAccountsFlow { sendResponse(it) }

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))
            }

        }
    }

    private suspend fun loadBanks(): List<Bank> {
        val response = bankRepository.getBankList().asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NoBanksFoundException()
        }
    }

    private suspend fun loadAccountsFlow(onComplete: (accounts: List<BankAccount>) -> Unit) {
        employeeRepository.getEmployeeBankAccounts(employeeId, EmployeeType.DRIVER, true)
            .asFlow().collect { response ->
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> onComplete(response.data ?: emptyList())
                }
            }
    }

    private fun sendResponse(bankAccList: List<BankAccount>) {
        if (bankAccList.isEmpty()) {
            setState(State.Empty)
        } else {
            _data.value = BankLFData(bankList = banks, bankAccList = bankAccList)

            if (state.value != State.Loaded) {
                setState(State.Loaded)
            }
        }
    }

    private fun setState(state: State) {
        _state.value = state
    }

    suspend fun updateMainAccount(newMainAccId: String) {
        val bankList = (data.value)!!.bankAccList
        val oldMainAccId = bankList.firstOrNull { it.mainAccount }?.id

        employeeRepository.updateMainAccount(
            employeeId,
            oldMainAccId,
            newMainAccId,
            EmployeeType.DRIVER
        )
    }

    fun setAdapterPos(pos: Int) {
        this._adapterPos = pos
    }

}

data class BankLFData(
    val bankList: List<Bank>,
    val bankAccList: List<BankAccount>
)