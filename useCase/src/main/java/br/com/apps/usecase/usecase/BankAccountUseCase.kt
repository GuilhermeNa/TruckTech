package br.com.apps.usecase.usecase

import br.com.apps.model.dto.bank.BankAccountDto
import br.com.apps.repository.repository.bank_account.BankAccountRepository
import br.com.apps.repository.util.WriteRequest
import br.com.apps.repository.util.validateAndProcess

class BankAccountUseCase(private val repository: BankAccountRepository) {

    suspend fun save(writeReq: WriteRequest<BankAccountDto>) {
        val dto = writeReq.data

        validateAndProcess (
            validateData = { dto.validateDataForDbInsertion() }
        ).let {
            repository.save(dto)
        }
    }

}