package br.com.apps.model.dto.payroll

import java.util.Date

data class AdvanceDto(
    val masterUid: String? = null,
    val id: String? = null,
    val travelId: String? = null,
    val employeeId: String? = null,

    val date: Date? = null,
    val value: Double? = null,
    @field:JvmField
    val isPaid: Boolean? = null,
    @field:JvmField
    val isApproved: Boolean? = null,
    val type: String? = null

) {

    fun validateFields(): Boolean {
        var isValid = true

        if (masterUid == null ||
            employeeId == null ||
            date == null ||
            value == null ||
            isPaid == null ||
            isApproved == null ||
            type == null
        ) {
            isValid = false
        }

        return isValid
    }

}