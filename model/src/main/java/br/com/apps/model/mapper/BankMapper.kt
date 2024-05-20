package br.com.apps.model.mapper

import br.com.apps.model.dto.bank.BankDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.bank.Bank

fun BankDto.toModel(): Bank {
    if (this.validateFields()) {
        return Bank(
            id = this.id!!,
            name = this.name!!,
            code = this.code!!.toInt(),
            urlImage = this.urlImage!!
        )
    }
    throw CorruptedFileException("BankMapper, toModel: ($this)")
}

fun Bank.toDto(): BankDto {
    return BankDto(
        id = this.id,
        name = this.name,
        code = this.code,
        urlImage = this.urlImage
    )
}
