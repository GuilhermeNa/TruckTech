package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

fun RefuelDto.toModel(): Refuel {
    return Refuel(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        travelId = this.travelId,
        costId = this.costId,
        date = this.date?.toLocalDateTime(),
        station = this.station,
        odometerMeasure = this.odometerMeasure?.toBigDecimal(),
        valuePerLiter = this.valuePerLiter?.toBigDecimal(),
        amountLiters = this.amountLiters?.toBigDecimal(),
        totalValue = this.totalValue?.toBigDecimal(),
        isCompleteRefuel = this.isCompleteRefuel
    )
}

fun Refuel.toDto(): RefuelDto {
    return RefuelDto(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        travelId = this.travelId,
        costId = this.costId,
        date = this.date?.toDate(),
        station = this.station,
        odometerMeasure = this.odometerMeasure?.toDouble(),
        valuePerLiter = this.valuePerLiter?.toDouble(),
        amountLiters = this.amountLiters?.toDouble(),
        totalValue = this.totalValue?.toDouble(),
        isCompleteRefuel = this.isCompleteRefuel
    )
}