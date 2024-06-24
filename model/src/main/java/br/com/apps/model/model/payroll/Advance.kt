package br.com.apps.model.model.payroll

import java.math.BigDecimal
import java.time.LocalDateTime

data class Advance(
    val masterUid: String,
    val id: String? = null,
    val travelId: String? = null,
    val employeeId: String,

    val date: LocalDateTime,
    val value: BigDecimal,
    @field:JvmField
    val isPaid: Boolean,
    @field:JvmField
    val isApproved: Boolean,
    val type: AdvanceType

) {


}

enum class AdvanceType(val description: String) {
    COMMISSION("COMMISSION"),
    PAYROLL("PAYROLL");

    companion object {
        fun getType(type: String): AdvanceType {
            return when (type) {
                "COMMISSION" -> COMMISSION
                "PAYROLL" -> PAYROLL
                else -> throw IllegalArgumentException()
            }
        }
    }

}
