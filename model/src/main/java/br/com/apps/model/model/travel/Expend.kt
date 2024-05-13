package br.com.apps.model.model.travel

import br.com.apps.model.model.label.Label
import java.math.BigDecimal
import java.time.LocalDateTime

data class Expend(
    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,
    val travelId: String? = null,
    var labelId: String? = null,

    var company: String? = null,
    var date: LocalDateTime? = null,
    var description: String? = null,
    var value: BigDecimal? = null,
    var label: Label? = null,

    var paidByEmployee: Boolean? = null,
    var alreadyRefunded: Boolean? = null

)