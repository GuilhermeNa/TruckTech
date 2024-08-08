package br.com.apps.repository.repository.bank_account

import br.com.apps.model.dto.bank.BankAccountDto

class BankAccountRepository(
    private val write: BankAccountWriteImpl,
    private val read: BankAccountReadImpl
) : BankAccountInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun delete(id: String) = write.delete(id)

    override suspend fun save(dto: BankAccountDto) = write.save(dto)

    override suspend fun updateMainAccount(oldMainAccId: String?, newMainAccId: String) =
        write.updateMainAccount(oldMainAccId, newMainAccId)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchBankAccsByEmployeeId(id: String, flow: Boolean) =
        read.fetchBankAccsByEmployeeId(id, flow)

    override suspend fun fetchBankAccById(id: String, flow: Boolean) =
        read.fetchBankAccById(id, flow)

}