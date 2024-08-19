package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.bank.BankAccountDto
import br.com.apps.model.enums.PixType.Companion.getPixTypeDescriptions
import br.com.apps.model.exceptions.null_objects.NullBankAccountException
import br.com.apps.model.exceptions.null_objects.NullBankException
import br.com.apps.model.expressions.atBrZone
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.util.toDate
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.repository.bank_account.BankAccountRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.BankAccountUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BankEditorViewModel(
    private val vmData: BankEVmData,
    private val bankAccRepo: BankAccountRepository,
    private val bankAccUseCase: BankAccountUseCase,
    private val bankRepository: BankRepository
) : ViewModel() {

    private var isEditing = vmData.bankAccountId != null

    private val _data = MutableLiveData<Response<BankEFData>>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    /**
     * Load data and send it back to View
     */
    private fun loadData() {
        viewModelScope.launch {
            val bankAcc = vmData.bankAccountId?.let { loadBankAccount(it) }
            val bankList = loadBanks()
            sendResponse(bankAcc, bankList)
        }
    }

    private suspend fun loadBanks(): List<Bank> {
        val response = bankRepository.fetchBankList()
            .asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data
                ?: throw NullBankException("Error when loading banks, data is null")
        }
    }

    private suspend fun loadBankAccount(bankAccId: String): BankAccount {
        val response = bankAccRepo.fetchBankAccById(bankAccId)
            .asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data
                ?: throw NullBankAccountException("Error when loading bank account, data is null")
        }
    }

    private fun sendResponse(bankAcc: BankAccount?, bankList: List<Bank>) {
        fun whenEditing(): BankEFData {
            bankAcc?.setBankById(bankList)
            return BankEFData(
                bankList = bankList,
                pixList = getPixTypeDescriptions().sorted(),
                bankAcc = bankAcc
            )
        }

        fun whenCreating(): BankEFData {
            return BankEFData(
                bankList = bankList,
                pixList = getPixTypeDescriptions().sorted()
            )
        }

        _data.value = Response.Success(if (isEditing) whenEditing() else whenCreating())
    }

    /**
     * Save data
     */
    fun save(viewDto: BankAccountDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                bankAccUseCase.save(WriteRequest(data = generateDto(viewDto)))
                emit(Response.Success())

            } catch (e: Exception) {
                emit(Response.Error(e))

            }
        }

    private fun generateDto(viewDto: BankAccountDto): BankAccountDto {
        fun whenEditing(): BankAccountDto {
            val bankAcc = (data.value as Response.Success).data?.bankAcc!!

            return viewDto.also {
                it.masterUid = bankAcc.masterUid
                it.id = bankAcc.id
                it.employeeId = bankAcc.employeeId
                it.insertionDate = bankAcc.insertionDate.toDate()
            }
        }

        fun whenCreating(): BankAccountDto = viewDto.also {
            it.insertionDate = LocalDateTime.now().atBrZone().toDate()
            it.masterUid = vmData.masterUid
            it.employeeId = vmData.employeeId
        }

        return if (isEditing) whenEditing()
        else whenCreating()
    }

    //---------------------------------------------------------------------------------------------//
    // VIEW METHODS
    //---------------------------------------------------------------------------------------------//

    fun validateBank(bankName: String): Boolean {
        var isValid = true

        if (bankName.isEmpty()) isValid = false

        val bankList = (data.value as Response.Success).data?.bankList!!
        if (!bankList.map { it.name }.contains(bankName)) isValid = false

        return isValid
    }

    fun getBankId(bankName: String): String {
        val bankList = (data.value as Response.Success).data?.bankList!!
        return bankList.firstOrNull { it.name == bankName }!!.id
    }

}

class BankEVmData(
    val masterUid: String,
    val employeeId: String,
    val bankAccountId: String?
)

data class BankEFData(
    val bankList: List<Bank>,
    val pixList: List<String>,
    val bankAcc: BankAccount? = null
)

