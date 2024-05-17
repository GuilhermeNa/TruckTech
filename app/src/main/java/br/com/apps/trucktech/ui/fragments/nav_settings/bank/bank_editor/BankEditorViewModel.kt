package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.factory.BankAccountFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.model.model.payment_method.PixType
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.EmployeeRepository
import kotlinx.coroutines.launch

class BankEditorViewModel(
    idHolder: IdHolder,
    private val repository: EmployeeRepository
) : ViewModel() {

    private val masterUid = idHolder.masterUid
        ?: throw NullPointerException("BankEditorViewModel: masterUid is null")

    private val employeeId = idHolder.driverId
        ?: throw NullPointerException("BankEditorViewModel: employeeId is null")

    private lateinit var bankAccount: BankAccount

    val descriptionList = PixType.getMappedPixTypeAndDescription().entries.map { it.value }

    private val _bankData = MutableLiveData<Response<BankAccount>>()
    val bankData get() = _bankData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        idHolder.bankAccountId?.let {
            loadData(it)
        }
    }

    private fun loadData(bankId: String) {
        viewModelScope.launch {
            repository.getBankAccountById(employeeId, bankId, EmployeeType.DRIVER)
                .asFlow().collect { response ->
                when(response) {
                    is Response.Error -> _bankData.value = response
                    is Response.Success -> response.data?.let {
                        bankAccount = it
                        _bankData.value = Response.Success(data = it)
                    }
                }
            }
        }
    }

    fun save(mappedFields: HashMap<String, String>) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = getBankAccDto(mappedFields)
                repository.saveBankAccount(dto, EmployeeType.DRIVER)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private fun getBankAccDto(mappedField: HashMap<String, String>): BankAccountDto {
        return if (::bankAccount.isInitialized) {
            BankAccountFactory.update(bankAccount, mappedField)
           bankAccount.toDto()
        } else {
            mappedField[BankAccountFactory.TAG_MASTER_UID] = masterUid
            mappedField[BankAccountFactory.TAG_EMPLOYEE_ID] = employeeId
            BankAccountFactory.create(mappedField).toDto()
        }
    }

}