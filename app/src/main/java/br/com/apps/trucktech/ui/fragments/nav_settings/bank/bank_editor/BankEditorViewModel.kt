package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.factory.BankAccountDtoFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.model.model.payment_method.PixType
import br.com.apps.repository.Response
import br.com.apps.usecase.EmployeeUseCase
import kotlinx.coroutines.launch

class BankEditorViewModel(
    private val employeeId: String,
    private val bankId: String?,
    private val useCase: EmployeeUseCase
) : ViewModel() {

    var bankAcc: BankAccount? = null
    val descriptionList = PixType.getMappedPixTypeAndDescription().entries.map { it.value }

    private val _loadBankAccount = MutableLiveData<Response<BankAccount>>()
    val loadBankAccount get() = _loadBankAccount

    init {
        bankId?.let {
            loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            useCase.getBankAccountById(employeeId, bankId!!, EmployeeType.DRIVER).asFlow().collect {
                _loadBankAccount.postValue(it)
            }
        }
    }

    fun saveBankAccount(bankAccDto: BankAccountDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                useCase.saveBankAccount(bankAccDto, EmployeeType.DRIVER)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    fun getBankAccDto(
        masterUid: String,
        bankName: String,
        branch: String,
        accNumber: String,
        type: String,
        pix: String
    ): BankAccountDto {
        return if(bankAcc == null) {
            BankAccountDtoFactory.create(
                masterUid = masterUid,
                employeeId = employeeId,
                bankName = bankName,
                branch = branch,
                accNumber = accNumber,
                type = PixType.getTypeInString(type),
                pix = pix,
                mainAccount = false
            )
        } else {
            bankAcc!!.toDto().also { dto ->
                dto.bankName = bankName
                dto.branch = branch.toInt()
                dto.accNumber = accNumber.toInt()
                dto.pixType = PixType.getTypeInString(type)
                dto.pix = pix
            }
        }
    }

}