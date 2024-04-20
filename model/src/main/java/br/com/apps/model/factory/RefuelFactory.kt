package br.com.apps.model.factory

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.toDate
import java.security.InvalidParameterException
import java.time.LocalDateTime

object RefuelFactory {

    fun createDto(
        nMasterUid: String?,
        nTravelId: String?,
        nTruckId: String?,
        nDate: LocalDateTime?,
        nStation: String?,
        nOdometer: String?,
        nAmountLiters: String?,
        nValuePerLiter: String?,
        nTotalValue: String?,
        nIsComplete: Boolean?
    ): RefuelDto {
        val masterUid = nMasterUid ?: throw InvalidParameterException("Null masterUid")
        val travelId = nTravelId ?: throw InvalidParameterException("Null travelId")
        val truckId = nTruckId ?: throw InvalidParameterException("Null truckId")
        val date = nDate ?: throw InvalidParameterException("Null date")
        val station = nStation ?: throw InvalidParameterException("Null station")
        val odometer = nOdometer ?: throw InvalidParameterException("Null odometer")
        val amountLiters = nAmountLiters ?: throw InvalidParameterException("Null amountLiters")
        val valuePerLiter = nValuePerLiter ?: throw InvalidParameterException("Null valuePerLiter")
        val totalValue = nTotalValue ?: throw InvalidParameterException("Null totalValue")
        val isComplete = nIsComplete ?: throw InvalidParameterException("Null isComplete")

        return RefuelDto(
            masterUid = masterUid,
            travelId = travelId,
            truckId = truckId,
            station = station,
            date = date.toDate(),
            odometerMeasure = odometer.toDouble(),
            amountLiters = amountLiters.toDouble(),
            valuePerLiter = valuePerLiter.toDouble(),
            totalValue = totalValue.toDouble(),
            isCompleteRefuel = isComplete
        )

    }
}