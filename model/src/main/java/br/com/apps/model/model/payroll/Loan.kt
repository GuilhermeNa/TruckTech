package br.com.apps.model.model.payroll

import java.math.BigDecimal
import java.time.LocalDateTime

data class Loan(
    val masterUid: String? = null,
    val id: String? = null,
    val employeeId: String? = null,

    val date: LocalDateTime? = null,
    val value: BigDecimal? = null,
    val installments: Int? = null,
    @field:JvmField
    val isPaid: Boolean? = null

) {

    fun getInstallmentValue(): BigDecimal {
        return value?.divide(installments?.let { BigDecimal(it) }) ?: BigDecimal.ZERO
    }


}
