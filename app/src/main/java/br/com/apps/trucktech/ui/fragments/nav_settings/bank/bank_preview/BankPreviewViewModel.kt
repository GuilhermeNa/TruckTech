package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.enums.WorkRole
import br.com.apps.model.exceptions.null_objects.NullBankAccountException
import br.com.apps.model.exceptions.null_objects.NullBankException
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BankPreviewViewModel(
    private val vmData: BankPVmData,
    private val repository: EmployeeRepository,
    private val bankRepository: BankRepository
) : ViewModel() {

    private lateinit var banks: List<Bank>

    /**
     * LiveData with a dark layer state, used when dialog is requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    /**
     * LiveData holding the response data of type [Response] with a [BankAccount]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<BankAccount>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            banks = loadBanks()
            loadBankAcc {
                sendResponse(it)
            }
        }
    }

    private suspend fun loadBanks(): List<Bank> {
        val response = bankRepository.fetchBankList().asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullBankException("Failed to found banks")
        }
    }

    private suspend fun loadBankAcc(complete: (bankAcc: BankAccount) -> Unit) {
        repository.getBankAccountById(
            vmData.employeeId,
            vmData.bankAccountId,
            WorkRole.TRUCK_DRIVER,
            true
        ).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data?.let(complete)
                    ?: throw NullBankAccountException()
            }
        }
    }

    private fun sendResponse(bankAcc: BankAccount) {
        bankAcc.setBankById(banks)
        _data.value = Response.Success(bankAcc)
    }

    suspend fun delete() =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                repository.deleteBankAcc(vmData.employeeId, vmData.bankAccountId, WorkRole.TRUCK_DRIVER)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    /**
     * Sets the visibility of the [_darkLayer] to true, indicating that it should be shown.
     */
    fun requestDarkLayer() {
        _darkLayer.value = true
    }

    /**
     * Sets the visibility of the [_darkLayer] to false, indicating that it should be dismissed.
     */
    fun dismissDarkLayer() {
        _darkLayer.value = false
    }

}

class BankPVmData(
    val employeeId: String,
    val bankAccountId: String
)
