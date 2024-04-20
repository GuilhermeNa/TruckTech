package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.travel.Travel

fun TravelDto.toModel(): Travel {
    return Travel(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        initialDate = this.initialDate?.toLocalDateTime(),
        finalDate = this.finalDate?.toLocalDateTime(),
        isFinished = this.isFinished,
        freightsList = this.freightsList,
        refuelsList = this.refuelsList,
        expendsList = this.expendsList
    )
}
