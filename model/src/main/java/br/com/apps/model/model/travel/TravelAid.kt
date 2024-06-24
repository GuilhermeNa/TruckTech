package br.com.apps.model.model.travel

import java.math.BigDecimal
import java.time.LocalDateTime

data class TravelAid(
    val masterUid: String,
    val id: String? = null,
    val employeeId: String,
    val travelId: String,

    val date: LocalDateTime,
    val value: BigDecimal,
    @field:JvmField
    val isPaid: Boolean
)