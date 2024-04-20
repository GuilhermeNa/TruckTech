package br.com.apps.model.model.payroll

import java.math.BigDecimal
import java.time.LocalDateTime

data class Advance(
    val masterUid: String? = null,
    val id: String? = null,
    val employeeId: String? = null,

    val date: LocalDateTime? = null,
    val value: BigDecimal? = null,
    @field:JvmField
    val isPaid: Boolean? = null

)
