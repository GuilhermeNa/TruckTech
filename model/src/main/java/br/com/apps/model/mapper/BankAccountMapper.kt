package br.com.apps.model.mapper

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.payment_method.PixType

fun BankAccountDto.toModel(): BankAccount {
    return BankAccount(
        masterUid = this.masterUid,
        id = this.id,
        employeeId = this.employeeId,
        bankName = this.bankName,
        branch = this.branch,
        accNumber = this.accNumber,
        pix = this.pix,
        image = this.image,
        mainAccount = this.mainAccount,
        pixType = this.pixType?.let { PixType.getType(it) }
    )
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
        image = this.image,
        mainAccount = this.mainAccount,
        pixType = this.pixType?.description
    )
}