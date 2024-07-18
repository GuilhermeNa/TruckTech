package br.com.apps.model.mapper

import br.com.apps.model.dto.bank.BankDto
import br.com.apps.model.model.bank.Bank

fun BankDto.toModel(): Bank {
    this.validateDataIntegrity()
    return Bank(
        id = this.id!!,
        name = this.name!!,
        code = this.code!!.toInt(),
        urlImage = this.urlImage!!
    )
}

fun Bank.toDto(): BankDto =
    BankDto(
        id = this.id,
        name = this.name,
        code = this.code,
        urlImage = this.urlImage
    )

