package br.com.apps.model.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Fine(
    val masterUid: String? = null,
    val id: String? = null,
    val expenseId: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val date: LocalDateTime? = null,
    val description: String? = null,
    val code: String? = null,
    val value: BigDecimal? = null

)