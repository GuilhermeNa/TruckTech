package br.com.apps.model.mapper

import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.model.Document
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

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

fun Document.toDto(): DocumentDto {
    return DocumentDto(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        expenseId = this.expenseId,
        labelId = this.labelId,

        name = this.name,
        urlImage = this.urlImage,
        plate = this.plate,
        expeditionDate = this.expeditionDate?.toDate(),
        expirationDate = this.expirationDate?.toDate()
    )
}

