package br.com.apps.model.dto.travel

import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import java.util.Date

data class TravelDto(
    val masterUid: String? = null,
    var id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    @field:JvmField
    val isFinished: Boolean? = false,
    val initialDate: Date? = null,
    val finalDate: Date? = null,
    val initialOdometerMeasurement: Double? = null,
    val finalOdometerMeasurement: Double? = null,

    val freightsList: List<Freight>? = null,
    val refuelsList: List<Refuel>? = null,
    val expendsList: List<Expend>? = null
) {

    fun validateFields(): Boolean {
        var isValid = true

        if (masterUid == null || truckId == null || driverId == null || isFinished == null ||
            initialDate == null || initialOdometerMeasurement == null
        ) {
            isValid = false
        }

        return isValid
    }

}


