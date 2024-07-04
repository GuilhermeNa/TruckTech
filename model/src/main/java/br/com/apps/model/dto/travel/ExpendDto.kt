package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.InvalidAuthLevelException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.user.PermissionLevelType
import java.util.Date

data class ExpendDto(
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var driverId: String? = null,
    var travelId: String? = null,
    val labelId: String? = null,

    var label: Label? = null,
    val company: String? = null,
    var date: Date? = null,
    val description: String? = null,
    val value: Double? = null,

    @field:JvmField
    var isPaidByEmployee: Boolean? = null,
    @field:JvmField
    var isAlreadyRefunded: Boolean? = null,
    @field:JvmField
    var isValid: Boolean? = null

) {

    fun validateFields(): Boolean {
        var areFieldsValid = true

        if (masterUid == null || truckId == null || travelId == null || driverId == null ||
            labelId == null || company == null || date == null || value == null || isPaidByEmployee == null ||
            isAlreadyRefunded == null || isValid == null
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
            labelId == null ||
            company == null ||
            date == null ||
            value == null ||
            isPaidByEmployee == null ||
            isAlreadyRefunded == null ||
            isValid == null
        ) throw InvalidForSavingException()
    }

}