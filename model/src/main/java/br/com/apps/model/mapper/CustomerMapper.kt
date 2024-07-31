package br.com.apps.model.mapper

import br.com.apps.model.dto.CustomerDto
import br.com.apps.model.model.Customer

fun CustomerDto.toModel(): Customer {
    validateDataIntegrity()
    return Customer(
        masterUid = masterUid!!,
        id = id!!,
        cnpj = cnpj!!,
        name = name!!
    )
}

fun Customer.toDto(): CustomerDto =
    CustomerDto(
        masterUid = this.masterUid,
        id = this.id,
        cnpj = this.cnpj,
        name = this.name
    )