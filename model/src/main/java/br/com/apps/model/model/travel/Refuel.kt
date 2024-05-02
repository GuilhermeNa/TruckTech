package br.com.apps.model.model.travel

import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal
import java.security.InvalidParameterException
import java.time.LocalDateTime

data class Refuel(
    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val travelId: String? = null,
    val costId: String? = null,
    val driverId: String? = null,

    var date: LocalDateTime? = null,
    var station: String? = null,
    var odometerMeasure: BigDecimal? = null,
    var valuePerLiter: BigDecimal? = null,
    var amountLiters: BigDecimal? = null,
    var totalValue: BigDecimal? = null,
    @field:JvmField
    var isCompleteRefuel: Boolean? = null
) {

    fun updateFields(mapFields: HashMap<String, String>) {
        mapFields.forEach { (key, value) ->
            when (key) {
                TAG_DATE -> this.date = value.toLocalDateTime()
                TAG_STATION -> this.station = value
                TAG_ODOMETER -> this.odometerMeasure = BigDecimal(value)
                TAG_VALUE_PER_LITER -> this.valuePerLiter = BigDecimal(value)
                TAG_AMOUNT_LITERS -> this.amountLiters = BigDecimal(value)
                TAG_TOTAL_VALUE -> this.totalValue = BigDecimal(value)
                TAG_IS_COMPLETE -> this.isCompleteRefuel = value.toBoolean()
                else -> throw InvalidParameterException("Impossible update this field")
            }
        }
    }

    companion object {
        const val TAG_DATE = "date"
        const val TAG_STATION = "station"
        const val TAG_ODOMETER = "odometerMeasure"
        const val TAG_VALUE_PER_LITER = "valuePerLiter"
        const val TAG_AMOUNT_LITERS = "amountLiters"
        const val TAG_TOTAL_VALUE = "totalValue"
        const val TAG_IS_COMPLETE = "isCompleteRefuel"
    }


}