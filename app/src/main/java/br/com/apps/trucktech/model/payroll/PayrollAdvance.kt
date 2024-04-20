package br.com.apps.trucktech.model.payroll

import java.math.BigDecimal
import java.time.LocalDateTime

data class PayrollAdvance(

    val id: Long,
    val date: LocalDateTime,
    val value: BigDecimal,
    val isPaid: Boolean

)
