package br.com.apps.model.model.order

import java.math.BigDecimal
import java.time.LocalDateTime

data class Order(
    val id: Long? = 0L,
    val requestId: Long? = 0L,
    val requesterId: Long? = 0L,
    val approverId: Long? = 0L,
    val paymentMethodId: Long? = 0L,
    val value: BigDecimal? = BigDecimal.ZERO,
    val date: LocalDateTime
    )