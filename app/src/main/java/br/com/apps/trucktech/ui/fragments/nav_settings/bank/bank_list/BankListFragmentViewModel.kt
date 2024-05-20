package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.State
import br.com.apps.usecase.EmployeeUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BankListFragmentViewModel(
    private val employeeId: String,
    private val useCase: EmployeeUseCase,
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

            val bankListDef = CompletableDeferred<List<Bank>>()
            loadBankList { bankListDef.complete(it) }

            val bankAccListDef = CompletableDeferred<List<BankAccount>>()
            launch {
                loadBankAccList {
                    if (isFirstBoot) {
                        isFirstBoot = false
                        bankAccListDef.complete(it)
                    }
                    else sendResponse(bankListDef.getCompleted(), it)
                }
            }

            awaitAll(bankListDef, bankAccListDef)
            sendResponse(bankListDef.getCompleted(), bankAccListDef.getCompleted())
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

    private suspend fun loadBankList(complete: (List<Bank>) -> Unit) {
        bankRepository.getBankList().asFlow().first { response ->
            when (response) {
                is Response.Error -> {
                    _state.value = State.Error(response.exception)
                    _data.value = response
                }

                is Response.Success -> response.data?.let { complete(it) }
            }
            true
        }
    }

    private suspend fun loadBankAccList(complete: (List<BankAccount>) -> Unit) {
        useCase.getEmployeeBankAccountsList(employeeId, EmployeeType.DRIVER).asFlow()
            .collect { response ->
                when (response) {
                    is Response.Error -> {
                        _state.value = State.Error(response.exception)
                        _data.value = response
                    }

                    is Response.Success -> response.data?.let {
                        complete(it)
                    }
                }
            }
    }

    suspend fun updateMainAccount(newMainAccId: String) {
        val bankList = (data.value as Response.Success).data!!.bankAccList

        val oldMainAccId = bankList.firstOrNull { it.mainAccount }?.id
        useCase.updateMainAccount(
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