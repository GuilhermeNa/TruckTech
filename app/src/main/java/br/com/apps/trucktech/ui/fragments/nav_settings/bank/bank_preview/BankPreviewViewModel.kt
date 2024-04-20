package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.Response
import br.com.apps.usecase.EmployeeUseCase
import kotlinx.coroutines.launch

class BankPreviewViewModel(
    private val employeeId: String,
    private val bankId: String,
    private val useCase: EmployeeUseCase
) : ViewModel() {

    private val _bankData = MutableLiveData<Response<BankAccount>>()
    val bankData get() = _bankData

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            useCase.getBankAccountById(employeeId, bankId, EmployeeType.DRIVER).asFlow().collect {
                _bankData.postValue(it)
            }
        }
    }

    suspend fun delete() =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                useCase.deleteBankAccount(employeeId, bankId, EmployeeType.DRIVER)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }


}