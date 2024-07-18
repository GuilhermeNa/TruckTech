package br.com.apps.model.mapper

import br.com.apps.model.dto.TruckDocumentDto
import br.com.apps.model.model.TruckDocument
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

fun TruckDocumentDto.toModel(): TruckDocument {
    return TruckDocument(
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

fun TruckDocument.toDto(): TruckDocumentDto =
    TruckDocumentDto(
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

