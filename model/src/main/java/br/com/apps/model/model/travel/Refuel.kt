package br.com.apps.model.model.travel

import java.math.BigDecimal
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

    fun validateIds(): Boolean {
        return !(masterUid.isNullOrBlank() ||
                id.isNullOrBlank() ||
                truckId.isNullOrBlank() ||
                driverId.isNullOrBlank() ||
                travelId.isNullOrBlank())
    }

}