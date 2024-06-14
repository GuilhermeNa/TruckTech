package br.com.apps.model.dto.payroll

import java.util.Date

data class TravelAidDto(
    val masterUid: String? = null,
    val id: String? = null,
    val employeeId: String? = null,

    val date: Date? = null,
    val value: Double? = null,
    @field:JvmField
    val isPaid: Boolean? = null
) {

    fun validateFields(): Boolean {
        var areFieldsValid = true

        if (masterUid == null ||
            employeeId == null ||
            date == null ||
            value == null ||
            isPaid == null
        ) {
            areFieldsValid = false
        }

        return areFieldsValid
    }

}