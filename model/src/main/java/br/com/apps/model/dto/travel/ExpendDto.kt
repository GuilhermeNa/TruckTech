package br.com.apps.model.dto.travel

import br.com.apps.model.model.label.Label
import java.util.Date

data class ExpendDto(
    val masterUid: String? = null,
    var id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,
    val travelId: String? = null,
    val labelId: String? = null,

    val company: String? = null,
    val date: Date? = null,
    val description: String? = null,
    val value: Double? = null,
    val label: Label? = null
)