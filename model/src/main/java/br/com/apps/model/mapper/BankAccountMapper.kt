package br.com.apps.model.mapper

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.payment_method.PixType

fun BankAccountDto.toModel(): BankAccount {

    if(this.validateFields()) {
        return BankAccount(
            masterUid = this.masterUid!!,
            id = this.id,
            employeeId = this.employeeId!!,
            bankName = this.bankName!!,
            branch = this.branch!!,
            accNumber = this.accNumber!!,
            code = this.code!!.toInt(),
            mainAccount = this.mainAccount!!,
            pix = this.pix,
            pixType = this.pixType?.let { PixType.getType(it) }
        )
    }

    throw CorruptedFileException("BankAccountMapper, toModel: ($this)")

}

fun BankAccount.toDto(): BankAccountDto {
    return BankAccountDto(
        masterUid = this.masterUid,
        id = this.id,
        employeeId = this.employeeId,
        bankName = this.bankName,
        branch = this.branch,
        accNumber = this.accNumber,
        pix = this.pix,
        code = this.code.toString(),
        mainAccount = this.mainAccount,
        pixType = this.pixType?.description
    )
}