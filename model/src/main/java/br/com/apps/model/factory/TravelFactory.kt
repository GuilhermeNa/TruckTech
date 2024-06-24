package br.com.apps.model.factory

import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal
import java.security.InvalidParameterException

object TravelFactory {

    fun create(viewDto: TravelDto): Travel {
        if (viewDto.validateFields()) {
            return Travel(
                masterUid = viewDto.masterUid!!,
                truckId = viewDto.truckId!!,
                driverId = viewDto.driverId!!,
                isFinished = viewDto.isFinished!!,
                initialDate = viewDto.initialDate!!.toLocalDateTime(),
                initialOdometerMeasurement = BigDecimal(viewDto.initialOdometerMeasurement!!)
            )
        }
        throw InvalidParameterException("TravelFactory, create: ($viewDto)")
    }

}
