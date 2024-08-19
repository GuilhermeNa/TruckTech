package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.exceptions.null_objects.NullBankAccountException
import br.com.apps.model.exceptions.null_objects.NullBankException
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.repository.bank_account.BankAccountRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BankPreviewViewModel(
    private val vmData: BankPVmData,
    private val bankAccRepo: BankAccountRepository,
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
        suspend fun loadBanks(): List<Bank> {
            val response = bankRepository.fetchBankList()
                .asFlow().first()
            return when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data
                    ?: throw NullBankException("Failed to found banks")
            }
        }

        suspend fun loadBankAcc(complete: (bankAcc: BankAccount) -> Unit) {
            bankAccRepo.fetchBankAccById(vmData.bankAccountId, true)
                .asFlow().collect { response ->
                    when (response) {
                        is Response.Error -> throw response.exception
                        is Response.Success -> response.data?.let(complete)
                            ?: throw NullBankAccountException()
                    }
                }
        }

        fun sendResponse(bankAcc: BankAccount) {
            bankAcc.setBankById(banks)
            _data.value = Response.Success(bankAcc)
        }

        viewModelScope.launch {
            banks = loadBanks()
            loadBankAcc { listBankAcc ->
                sendResponse(listBankAcc)
            }
        }
    }

    suspend fun delete() =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                bankAccRepo.delete(vmData.bankAccountId)
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
