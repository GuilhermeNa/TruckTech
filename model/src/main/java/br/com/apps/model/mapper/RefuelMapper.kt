package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.travel.Refuel

fun RefuelDto.toModel(): Refuel {
    return Refuel(
        id = this.id,
        truckId = this.truckId,
        travelId = this.travelId,
    )
}