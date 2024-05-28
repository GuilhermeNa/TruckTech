package br.com.apps.model.model.travel

import java.math.BigDecimal
import java.time.LocalDateTime

data class Refuel(
    val masterUid: String,
    val id: String? = null,
    val truckId: String,
    val travelId: String? = null,
    val costId: String? = null,
    val driverId: String? = null,

    var date: LocalDateTime,
    var station: String,
    var odometerMeasure: BigDecimal,
    var valuePerLiter: BigDecimal,
    var amountLiters: BigDecimal,
    var totalValue: BigDecimal,
    @field:JvmField
    var isCompleteRefuel: Boolean
) {

}