package br.com.apps.model.mapper

import br.com.apps.model.dto.CustomerDto
import br.com.apps.model.model.Customer

fun CustomerDto.toModel(): Customer {
    return Customer(
        masterUid = this.masterUid!!,
        id = this.id,
        cnpj = this.cnpj!!,
        name = this.name!!
    )
}

fun Customer.toDto(): CustomerDto {
    return CustomerDto(
        masterUid = this.masterUid,
        id = this.id,
        cnpj = this.cnpj,
        name = this.name
    )
}