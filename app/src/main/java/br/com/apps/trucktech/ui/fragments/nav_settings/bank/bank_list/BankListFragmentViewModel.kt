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
import br.com.apps.trucktech.util.State
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BankListFragmentViewModel(
    private val employeeId: String,
    private val employeeRepository: EmployeeRepository,
    private val bankRepository: BankRepository
) : ViewModel() {

    private var isFirstBoot = true

    private val _data = MutableLiveData<Response<BankLFData>>()
    val data get() = _data

    private val _state = MutableLiveData<State>(State.Loading)
    val state get() = _state

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val bankListDef = loadBankList()
            val bankAccListDef = CompletableDeferred<List<BankAccount>>()

            launch {
                loadBankAccList { data ->
                    if (isFirstBoot) {
                        isFirstBoot = false
                        bankAccListDef.complete(data)
                    } else sendResponse(bankListDef.getCompleted(), data)
                }
            }

            val bankList = bankListDef.await()
            val accList = bankAccListDef.await()

            sendResponse(bankList, accList)
        }
    }

    private fun sendResponse(bankList: List<Bank>, bankAccList: List<BankAccount>) {
        _state.value =
            if (bankAccList.isEmpty()) State.Empty
            else State.Loaded

        _data.value = Response.Success(
            BankLFData(bankList = bankList, bankAccList = bankAccList)
        )
    }

    private suspend fun loadBankList(): CompletableDeferred<List<Bank>> {
        val deferred = CompletableDeferred<List<Bank>>()

        bankRepository.getBankList().asFlow().first { response ->
            when (response) {
                is Response.Error -> {
                    _state.value = State.Error(response.exception)
                    _data.value = response
                }

                is Response.Success -> response.data?.let { deferred.complete(it) }
            }
            true
        }

        return deferred
    }

    private suspend fun loadBankAccList(complete: (List<BankAccount>) -> Unit) {
        employeeRepository.getEmployeeBankAccounts(employeeId, EmployeeType.DRIVER, true).asFlow()
            .collect { response ->
                when (response) {
                    is Response.Error -> {
                        _state.value = State.Error(response.exception)
                        _data.value = response
                    }

                    is Response.Success -> response.data?.let { complete(it) }
                }
            }
    }

    suspend fun updateMainAccount(newMainAccId: String) {
        val bankList = (data.value as Response.Success).data!!.bankAccList

        val oldMainAccId = bankList.firstOrNull { it.mainAccount }?.id
        employeeRepository.updateMainAccount(
            employeeId,
            oldMainAccId,
            newMainAccId,
            EmployeeType.DRIVER
        )

    }
}

data class BankLFData(
    val bankList: List<Bank>,
    val bankAccList: List<BankAccount>
)