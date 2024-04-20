package br.com.apps.model.dto.travel

import java.util.Date

data class FreightDto (
    val id: String? = null,
    val incomeId: String? = null,
    val truckId: String? = null,
    val travelId: String? = null,

    val origin: String? = null,
    val destiny: String? = null,
    val cargo: String? = null,
    val breakDown: Double? = null,
    val date: Date? = null,
    val type: String? = null
)