package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.util.Response
import br.com.apps.usecase.EmployeeUseCase
import kotlinx.coroutines.launch

class BankListFragmentViewModel(
    private val employeeId: String,
    private val useCase: EmployeeUseCase
) : ViewModel() {

    lateinit var bankList: List<BankAccount>

    private val _bankData = MutableLiveData<Response<List<BankAccount>>>()
    val bankData get() = _bankData

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            useCase.getEmployeeBankAccountsList(employeeId, EmployeeType.DRIVER).asFlow().collect {
                _bankData.postValue(it)
            }
        }
    }

    suspend fun updateMainAccount(newMainAccId: String) {
        if (::bankList.isInitialized) {
            val oldMainAccId = bankList.firstOrNull { it.mainAccount == true }?.id
            useCase.updateMainAccount(
                employeeId,
                oldMainAccId,
                newMainAccId,
                EmployeeType.DRIVER
            )
        }
    }

}