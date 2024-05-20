package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BankPreviewViewModel(
    private val idHolder: IdHolder,
    private val repository: EmployeeRepository,
    private val bankRepository: BankRepository
) : ViewModel() {

    private val employeeId = idHolder.driverId
        ?: throw NullPointerException("BankPreviewViewModel: employeeId is null")

    private val bankId = idHolder.bankAccountId
        ?: throw NullPointerException("BankPreviewViewModel: bankId is null")

    private var isFirstBoot = true

    /**
     * LiveData with a dark layer state, used when dialog is requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    /**
     * LiveData holding the response data of type [Response] with a [BankAccount]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<Response<BankPFData>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {

            val bankListDef = loadBankList()

            val bankAccDef = CompletableDeferred<BankAccount>()
            launch {
                loadBankAcc {
                    if(isFirstBoot) {
                        isFirstBoot = false
                        bankAccDef.complete(it)
                    } else {
                        sendResponse(bankListDef.getCompleted(), it)
                    }
                }
            }

            awaitAll(bankListDef, bankAccDef)
            sendResponse(bankListDef.getCompleted(), bankAccDef.getCompleted())

        }
    }

    private suspend fun loadBankList(): CompletableDeferred<List<Bank>> {
        val deferred = CompletableDeferred<List<Bank>>()

        bankRepository.getBankList().asFlow().first { response ->
            when(response) {
                is Response.Error -> {}
                is Response.Success -> response.data?.let { deferred.complete(it) }
            }
            true
        }

        return deferred
    }

    private suspend fun loadBankAcc(complete: (bankAcc: BankAccount) -> Unit) {
        repository.getBankAccountById(employeeId, bankId, EmployeeType.DRIVER, true).asFlow()
            .collect { response ->
                when (response) {
                    is Response.Error -> {}
                    is Response.Success -> response.data?.let { complete(it) }
                }
            }
    }

    private fun sendResponse(bankList: List<Bank>, bankAcc: BankAccount) {
        val urlImage = bankList.firstOrNull { it.code == bankAcc.code }?.urlImage
        _data.value = Response.Success(BankPFData(bankAcc = bankAcc, urlImage = urlImage))
    }

    suspend fun delete() =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                repository.deleteBankAcc(employeeId, bankId, EmployeeType.DRIVER)
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

data class BankPFData(
    val bankAcc: BankAccount,
    val urlImage: String?

)
