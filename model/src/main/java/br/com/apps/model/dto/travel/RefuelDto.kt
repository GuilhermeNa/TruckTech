package br.com.apps.model.dto.travel

import java.util.Date

data class RefuelDto(
    val masterUid: String? = null,
    var id: String? = null,
    val truckId: String? = null,
    val travelId: String? = null,
    val costId: String? = null,
    val driverId: String? = null,

    val date: Date? = null,
    val station: String? = null,
    val odometerMeasure: Double? = null,
    val valuePerLiter: Double? = null,
    val amountLiters: Double? = null,
    val totalValue: Double? = null,
    @field:JvmField
    val isCompleteRefuel: Boolean? = null
) {

    fun validateFields(): Boolean {
        var isValid = true

        if (masterUid == null || truckId == null || travelId == null || driverId == null || date == null ||
            station == null || odometerMeasure == null || amountLiters == null || totalValue == null ||
            isCompleteRefuel == null
        ) {
            isValid = false
        }

        return isValid
    }

}