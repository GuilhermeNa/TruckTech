package br.com.apps.model.mapper

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.payment_method.PixType
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

fun BankAccountDto.toModel(): BankAccount {
    this.validateDataIntegrity()
    return BankAccount(
        masterUid = this.masterUid!!,
        id = this.id,
        employeeId = this.employeeId!!,
        insertionDate = this.insertionDate!!.toLocalDateTime(),
        bankName = this.bankName!!,
        branch = this.branch!!,
        accNumber = this.accNumber!!,
        code = this.code!!.toInt(),
        mainAccount = this.mainAccount!!,
        pix = this.pix,
        pixType = this.pixType?.let { PixType.getType(it) }
    )
}

fun BankAccount.toDto(): BankAccountDto =
    BankAccountDto(
        masterUid = this.masterUid,
        id = this.id,
        employeeId = this.employeeId,
        insertionDate = this.insertionDate.toDate(),
        bankName = this.bankName,
        branch = this.branch,
        accNumber = this.accNumber,
        pix = this.pix,
        code = this.code.toString(),
        mainAccount = this.mainAccount,
        pixType = this.pixType?.description
    )
