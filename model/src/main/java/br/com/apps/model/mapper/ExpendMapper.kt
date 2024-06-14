package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

fun ExpendDto.toModel(): Expend {

    if (this.validateFields()) {
        return Expend(
            masterUid = this.masterUid!!,
            id = this.id,
            truckId = this.truckId!!,
            driverId = this.driverId!!,
            travelId = this.travelId!!,
            labelId = this.labelId!!,
            date = this.date!!.toLocalDateTime(),
            value = this.value!!.toBigDecimal(),
            description = this.description!!,
            company = this.company!!,
            label = this.label,
            isPaidByEmployee = this.isPaidByEmployee!!,
            isAlreadyRefunded = this.isAlreadyRefunded!!,
            isValid = this.isValid!!
        )
    }

    throw CorruptedFileException("ExpendMapper, toModel: ($this)")
}

fun Expend.toDto(): ExpendDto {
    return ExpendDto(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        travelId = this.travelId,
        labelId = this.labelId,
        date = this.date.toDate(),
        value = this.value.toDouble(),
        description = this.description,
        company = this.company,
        label = this.label,
        isPaidByEmployee = this.isPaidByEmployee,
        isAlreadyRefunded = this.isAlreadyRefunded,
        isValid = this.isValid
    )
}
