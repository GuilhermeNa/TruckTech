package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.travel.Expend

fun ExpendDto.toModel(): Expend {
    return Expend(
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        travelId = this.travelId
    )
}