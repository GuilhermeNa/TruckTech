package br.com.apps.model.factory

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

object RefuelFactory {

    fun create(viewDto: RefuelDto): Refuel {
        viewDto.validateDataIntegrity()
        return Refuel(
            masterUid = viewDto.masterUid!!,
            travelId = viewDto.travelId,
            truckId = viewDto.truckId!!,
            driverId = viewDto.driverId,
            station = viewDto.station!!,
            date = viewDto.date!!.toLocalDateTime(),
            odometerMeasure = BigDecimal(viewDto.odometerMeasure!!),
            amountLiters = BigDecimal(viewDto.amountLiters!!),
            valuePerLiter = BigDecimal(viewDto.valuePerLiter!!),
            totalValue = BigDecimal(viewDto.totalValue!!),
            isCompleteRefuel = viewDto.isCompleteRefuel!!,
            isValid = viewDto.isValid!!
        )
    }

    fun update(refuel: Refuel, viewDto: RefuelDto) {
        viewDto.date?.let { refuel.date = it.toLocalDateTime() }
        viewDto.station?.let { refuel.station = it }
        viewDto.odometerMeasure?.let { refuel.odometerMeasure = BigDecimal(it) }
        viewDto.valuePerLiter?.let { refuel.valuePerLiter = BigDecimal(it) }
        viewDto.amountLiters?.let { refuel.amountLiters = BigDecimal(it) }
        viewDto.totalValue?.let { refuel.totalValue = BigDecimal(it) }
        viewDto.isCompleteRefuel?.let { refuel.isCompleteRefuel = it }
        viewDto.isValid?.let { refuel.isValid = it }
    }

}