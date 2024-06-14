package br.com.apps.model.model.travel

import br.com.apps.model.model.label.Label
import java.math.BigDecimal
import java.time.LocalDateTime

data class Expend(
    val masterUid: String,
    val id: String? = null,
    val truckId: String,
    val driverId: String,
    val travelId: String,
    var labelId: String,

    var label: Label? = null,
    var company: String,
    var date: LocalDateTime,
    var description: String,
    var value: BigDecimal,

    @field:JvmField
    var isPaidByEmployee: Boolean,
    @field:JvmField
    var isAlreadyRefunded: Boolean,
    @field:JvmField
    var isValid: Boolean

)