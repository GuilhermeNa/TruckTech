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
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.model.model.payment_method.PixType
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BankEditorViewModel(
    private val idHolder: IdHolder,
    private val employeeRepository: EmployeeRepository,
    private val bankRepository: BankRepository
) : ViewModel() {

    private val masterUid = idHolder.masterUid
        ?: throw NullPointerException("BankEditorViewModel: masterUid is null")

    private val employeeId = idHolder.driverId
        ?: throw NullPointerException("BankEditorViewModel: employeeId is null")

    private var isEditing = false

    private val _data = MutableLiveData<Response<BankEditorData>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        isEditing = idHolder.bankAccountId?.let { true } ?: false

        loadData()

    }

    /**
     * Load data and send it back to View
     */
    private fun loadData() {
        viewModelScope.launch {

            val bankAccDef = idHolder.bankAccountId?.let { loadBankAccount(it) }
            val bankListDef = loadBanks()

            _data.value =
                if (isEditing) responseWhenEditing(bankAccDef, bankListDef)
                else responseWhenCreating(bankListDef)

        }
    }

    private suspend fun loadBanks(): CompletableDeferred<List<Bank>> {
        val bankListDef = CompletableDeferred<List<Bank>>()

        bankRepository.getBankList().asFlow().first { response ->
            when (response) {
                is Response.Error -> _data.value = response
                is Response.Success -> {

                    response.data?.let {
                        bankListDef.complete(it)
                    }
                }
            }
            true
        }

        return bankListDef
    }

    private suspend fun loadBankAccount(bankId: String): CompletableDeferred<BankAccount> {
        val bankAccDef = CompletableDeferred<BankAccount>()

        employeeRepository.getBankAccountById(employeeId, bankId, EmployeeType.DRIVER)
            .asFlow().first { response ->
                when (response) {
                    is Response.Error -> _data.value = response
                    is Response.Success -> response.data?.let { bankAccDef.complete(it) }
                }
                true
            }

        return bankAccDef
    }

    private suspend fun responseWhenCreating(bankListDef: CompletableDeferred<List<Bank>>): Response.Success<BankEditorData> {
        bankListDef.await()
        val bankList = bankListDef.getCompleted()
        return Response.Success(
            BankEditorData(
                bankList = bankList,
                pixList = PixType.getMappedPixTypeAndDescription().entries.map { it.value }
            )
        )
    }

    private suspend fun responseWhenEditing(
        bankAccDef: CompletableDeferred<BankAccount>?,
        bankListDef: CompletableDeferred<List<Bank>>
    ): Response.Success<BankEditorData> {
        awaitAll(bankAccDef!!, bankListDef)
        val bankList = bankListDef.getCompleted()
        val bankAcc = bankAccDef.getCompleted()
        return Response.Success(
            BankEditorData(
                bankList = bankList,
                pixList = PixType.getMappedPixTypeAndDescription().entries.map { it.value },
                bankAcc = bankAcc
            )
        )
    }

    /**
     * Save data
     */
    fun save(viewDto: BankAccountDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = createOrUpdate(viewDto)
                employeeRepository.saveBankAccount(dto, EmployeeType.DRIVER)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private fun createOrUpdate(viewDto: BankAccountDto): BankAccountDto {
        return if (isEditing) {
            val bankAcc = (data.value as Response.Success).data?.bankAcc
            BankAccountFactory.update(bankAcc!!, viewDto)
            bankAcc.toDto()

        } else {
            viewDto.run {
                this.masterUid = this@BankEditorViewModel.masterUid
                this.employeeId = this@BankEditorViewModel.employeeId
            }
            BankAccountFactory.create(viewDto).toDto()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // VIEW METHODS
    //---------------------------------------------------------------------------------------------//

    fun validateBank(bankName: String): Boolean {
        var isValid = true

        if(bankName.isEmpty()) isValid = false

        val bankList = (data.value as Response.Success).data?.bankList!!
        if(!bankList.map { it.name }.contains(bankName)) isValid = false

        return isValid
    }

    fun getBankCode(bankName: String): String {
        val bankList = (data.value as Response.Success).data?.bankList!!
        return bankList.firstOrNull { it.name == bankName }?.code.toString()
    }

}

data class BankEditorData(
    val bankList: List<Bank>,
    val pixList: List<String>,
    val bankAcc: BankAccount? = null
)