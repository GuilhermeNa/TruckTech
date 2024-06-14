package br.com.apps.model.model.payroll

import java.math.BigDecimal
import java.time.LocalDateTime

data class TravelAid(
    val masterUid: String,
    val id: String? = null,
    val employeeId: String,

    val date: LocalDateTime,
    val value: BigDecimal,
    @field:JvmField
    val isPaid: Boolean
)