package br.com.apps.model.factory

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.payment_method.PixType
import br.com.apps.model.toLocalDateTime
import java.security.InvalidParameterException

object BankAccountFactory {

    fun create(viewDto: BankAccountDto): BankAccount {
        if (viewDto.validateFields()) {
            return BankAccount(
                masterUid = viewDto.masterUid!!,
                employeeId = viewDto.employeeId!!,
                insertionDate = viewDto.insertionDate!!.toLocalDateTime(),
                code = viewDto.code!!.toInt(),
                bankName = viewDto.bankName!!,
                branch = viewDto.branch!!,
                accNumber = viewDto.accNumber!!,
                mainAccount = viewDto.mainAccount!!,
                pix = viewDto.pix,
                pixType = viewDto.pixType?.let { PixType.getType(it) }
            )
        }
        throw InvalidParameterException("BankAccountFactory, create: ($viewDto)")
    }

    fun update(bankAccount: BankAccount, viewDto: BankAccountDto) {
        viewDto.code?.run { bankAccount.code = this.toInt() }
        viewDto.bankName?.run { bankAccount.bankName = this }
        viewDto.branch?.run { bankAccount.branch = this }
        viewDto.accNumber?.run { bankAccount.accNumber = this }
        viewDto.pixType?.run { bankAccount.pixType = PixType.getType(this) }
        viewDto.pix?.run { bankAccount.pix = this }
        viewDto.mainAccount?.run { bankAccount.mainAccount = this }
    }

}

