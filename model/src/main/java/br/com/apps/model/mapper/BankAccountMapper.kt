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
        id = this.id!!,
        employeeId = this.employeeId!!,
        bankId = this.bankId!!,
        insertionDate = this.insertionDate!!.toLocalDateTime(),
        branch = this.branch!!,
        accNumber = this.accNumber!!,
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
        bankId = this.bankId,
        insertionDate = this.insertionDate.toDate(),
        branch = this.branch,
        accNumber = this.accNumber,
        pix = this.pix,
        mainAccount = this.mainAccount,
        pixType = this.pixType?.description
    )
