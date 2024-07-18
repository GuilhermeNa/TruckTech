package br.com.apps.model.model.travel

import java.math.BigDecimal
import java.time.LocalDateTime

data class TravelAid(
    val masterUid: String,
    var id: String? = null,
    val driverId: String,
    val travelId: String,
    val date: LocalDateTime,
    var value: BigDecimal,
    @field:JvmField
    val isPaid: Boolean
)