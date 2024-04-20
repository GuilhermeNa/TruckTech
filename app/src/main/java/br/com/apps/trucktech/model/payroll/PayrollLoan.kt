package br.com.apps.trucktech.model.payroll

import java.math.BigDecimal
import java.time.LocalDateTime

data class PayrollLoan(

    val id: Long,
    val date: LocalDateTime,
    val value: BigDecimal,
    val installmentsNumber: Int,
    val installmentsValue: BigDecimal,
    val isPaid: Boolean

)
