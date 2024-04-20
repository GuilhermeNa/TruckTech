package br.com.apps.trucktech.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Fine(

    val id: Long? = null,
    val truckId: Long,
    val driverId: Long,

    val date: LocalDateTime,
    val code: String,
    val description: String,
    val value: BigDecimal

)