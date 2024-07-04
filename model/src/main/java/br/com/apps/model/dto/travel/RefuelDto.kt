package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.InvalidAuthLevelException
import br.com.apps.model.model.user.PermissionLevelType
import java.util.Date

data class RefuelDto(
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var travelId: String? = null,
    val costId: String? = null,
    var driverId: String? = null,

    var date: Date? = null,
    val station: String? = null,
    val odometerMeasure: Double? = null,
    val valuePerLiter: Double? = null,
    val amountLiters: Double? = null,
    val totalValue: Double? = null,

    @field:JvmField
    val isCompleteRefuel: Boolean? = null,

    @field:JvmField
    var isValid: Boolean? = null

) {

    fun validateFields(): Boolean {
        var areFieldsValid = true

        if (masterUid == null ||
            truckId == null ||
            travelId == null ||
            driverId == null ||
            date == null ||
            station == null ||
            odometerMeasure == null ||
            valuePerLiter == null ||
            amountLiters == null ||
            totalValue == null ||
            isCompleteRefuel == null ||
            isValid == null
        ) {
            areFieldsValid = false
        }

        return areFieldsValid
    }

    fun validatePermission(authLevel: PermissionLevelType?) {
        if (authLevel == null) throw NullPointerException("AuthLevel is null")
        if (isValid == null) throw NullPointerException("isValid is null")
        if (isValid!! && authLevel != PermissionLevelType.MANAGER) throw InvalidAuthLevelException()

    }

    fun validateDataForSaving() {
        if (masterUid == null ||
            truckId == null ||
            travelId == null ||
            driverId == null ||
            date == null ||
            station == null ||
            odometerMeasure == null ||
            valuePerLiter == null ||
            amountLiters == null ||
            totalValue == null ||
            isCompleteRefuel == null ||
            isValid == null
        ) throw InvalidAuthLevelException()
    }

}