package br.com.apps.model.model.finance

import java.math.BigDecimal

data class Expense(
    val id: Long = 0L,
    val truckId: Long = 0L,
    val driverId: Long = 0L,
    val labelId: Long = 0L,

    val value: BigDecimal = BigDecimal.ZERO,
    val type: ExpenseType
)

enum class ExpenseType {
    FIXED, VARIABLE
}