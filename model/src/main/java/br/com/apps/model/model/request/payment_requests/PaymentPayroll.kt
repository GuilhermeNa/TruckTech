package br.com.apps.model.model.request.payment_requests

import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentPayroll(

    val id: Long?,
    val date: LocalDateTime,
    val value: BigDecimal,
    val type: PaymentPayrollType

) {

}

enum class PaymentPayrollType {
    COMMISSION, VACATION, REMUNERATION, BONUS, THIRTEENTH_FIRST, THIRTEENTH_SECOND

}