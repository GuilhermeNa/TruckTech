package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

fun ExpendDto.toModel(): Expend {
    return Expend(
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        travelId = this.travelId,
        labelId = this.labelId,
        date = this.date?.toLocalDateTime(),
        value = this.value?.toBigDecimal(),
        description = this.description,
        company = this.company,
        label = this.label
    )
}

fun Expend.toDto(): ExpendDto {
    return ExpendDto(
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        travelId = this.travelId,
        labelId = this.labelId,
        date = this.date?.toDate(),
        value = this.value?.toDouble(),
        description = this.description,
        company = this.company,
        label = this.label
    )
}