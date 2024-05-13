package br.com.apps.model.factory

import br.com.apps.model.factory.FactoryUtil.Companion.checkIfStringsAreBlank
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal
import java.security.InvalidParameterException

object RefuelFactory {

    const val TAG_MASTER_UID = "masterUid"
    const val TAG_TRUCK_ID = "truckId"
    const val TAG_TRAVEL_ID = "travelId"
    const val TAG_DRIVER_ID = "driverId"
    const val TAG_DATE = "date"
    const val TAG_STATION = "station"
    const val TAG_ODOMETER = "odometerMeasure"
    const val TAG_VALUE_PER_LITER = "valuePerLiter"
    const val TAG_AMOUNT_LITERS = "amountLiters"
    const val TAG_TOTAL_VALUE = "totalValue"
    const val TAG_IS_COMPLETE = "isCompleteRefuel"

    fun create(mappedFields: HashMap<String, String>): Refuel {
        val masterUid = mappedFields[TAG_MASTER_UID]
            ?: throw NullPointerException("RefuelFactory, create: masterUid is null")

        val travelId = mappedFields[TAG_TRAVEL_ID]
            ?: throw NullPointerException("RefuelFactory, create: travelId is null")

        val truckId = mappedFields[TAG_TRUCK_ID]
            ?: throw NullPointerException("RefuelFactory, create: truckId is null")

        val driverId = mappedFields[TAG_DRIVER_ID]
            ?: throw NullPointerException("RefuelFactory, create: driverId is null")

        val date = mappedFields[TAG_DATE]
            ?: throw NullPointerException("RefuelFactory, create: date is null")

        val station = mappedFields[TAG_STATION]
            ?: throw NullPointerException("RefuelFactory, create: station is null")

        val odometerMeasure = mappedFields[TAG_ODOMETER]
            ?: throw NullPointerException("RefuelFactory, create: odometerMeasure is null")

        val valuePerLiter = mappedFields[TAG_VALUE_PER_LITER]
            ?: throw NullPointerException("RefuelFactory, create: valuePerLiter is null")

        val amountOfLiters = mappedFields[TAG_AMOUNT_LITERS]
            ?: throw NullPointerException("RefuelFactory, create: amountOfLiters is null")

        val totalValue = mappedFields[TAG_TOTAL_VALUE]
            ?: throw NullPointerException("RefuelFactory, create: totalValue is null")

        val isComplete = mappedFields[TAG_IS_COMPLETE]
            ?: throw NullPointerException("RefuelFactory, create: isComplete is null")

        checkIfStringsAreBlank(
            masterUid, travelId, truckId, driverId, date, station, odometerMeasure,
            valuePerLiter, amountOfLiters, totalValue, isComplete
        )


        return Refuel(
            masterUid = masterUid,
            travelId = travelId,
            truckId = truckId,
            driverId = driverId,
            station = station,
            date = date.toLocalDateTime(),
            odometerMeasure = BigDecimal(odometerMeasure),
            amountLiters = BigDecimal(amountOfLiters),
            valuePerLiter = BigDecimal(valuePerLiter),
            totalValue = BigDecimal(totalValue),
            isCompleteRefuel = isComplete.toBoolean()
        )

    }

    fun update(refuel: Refuel, mappedFields: HashMap<String, String>) {
        mappedFields.forEach { (key, value) ->
            when (key) {
                TAG_DATE -> refuel.date = value.toLocalDateTime()
                TAG_STATION -> refuel.station = value
                TAG_ODOMETER -> refuel.odometerMeasure = BigDecimal(value)
                TAG_VALUE_PER_LITER -> refuel.valuePerLiter = BigDecimal(value)
                TAG_AMOUNT_LITERS -> refuel.amountLiters = BigDecimal(value)
                TAG_TOTAL_VALUE -> refuel.totalValue = BigDecimal(value)
                TAG_IS_COMPLETE -> refuel.isCompleteRefuel = value.toBoolean()
                else -> throw InvalidParameterException("RefuelFactory, create: impossible update this field $key")
            }
        }
    }

}