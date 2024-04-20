package br.com.apps.model.mapper

import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.model.Document

fun DocumentDto.toModel(): Document {
    return Document(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        expenseId = this.expenseId,
        labelId = this.labelId,
        name = this.name,
        urlImage = this.urlImage,
        plate = this.plate,
        expeditionDate = this.expeditionDate?.toLocalDateTime(),
        expirationDate = this.expirationDate?.toLocalDateTime()
    )
}

