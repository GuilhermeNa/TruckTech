package br.com.apps.model.dto.travel

import br.com.apps.model.dto.DtoObjectsInterface
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidAuthLevelException
import br.com.apps.model.exceptions.InvalidForSavingException
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
    var station: String? = null,
    var odometerMeasure: Double? = null,
    var valuePerLiter: Double? = null,
    var amountLiters: Double? = null,
    var totalValue: Double? = null,
    @field:JvmField
    var isCompleteRefuel: Boolean? = null,
    @field:JvmField
    var isValid: Boolean? = null
) : DtoObjectsInterface {

    override fun validateDataIntegrity() {
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
        ) throw CorruptedFileException("RefuelDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {
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
        ) throw InvalidForSavingException("RefuelDto data is invalid: ($this)")
    }

    fun validatePermission(authLevel: PermissionLevelType?) {
        if (authLevel == null) throw NullPointerException("AuthLevel is null")
        if (isValid == null) throw NullPointerException("isValid is null")
        if (isValid!! && authLevel != PermissionLevelType.MANAGER) throw InvalidAuthLevelException()
    }

}