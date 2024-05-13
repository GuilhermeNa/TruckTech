package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

fun TravelDto.toModel(): Travel {
    return Travel(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        initialDate = this.initialDate?.toLocalDateTime(),
        finalDate = this.finalDate?.toLocalDateTime(),
        initialOdometerMeasurement = this.initialOdometerMeasurement?.toBigDecimal(),
        finalOdometerMeasurement = this.finalOdometerMeasurement?.toBigDecimal(),
        isFinished = this.isFinished,
        freightsList = this.freightsList,
        refuelsList = this.refuelsList,
        expendsList = this.expendsList
    )
}

fun Travel.toDto(): TravelDto {
    return TravelDto(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        initialDate = this.initialDate?.toDate(),
        finalDate = this.finalDate?.toDate(),
        initialOdometerMeasurement = this.initialOdometerMeasurement?.toDouble(),
        finalOdometerMeasurement = this.finalOdometerMeasurement?.toDouble(),
        isFinished = this.isFinished,
        freightsList = null,
        refuelsList = null,
        expendsList = null
    )
}
