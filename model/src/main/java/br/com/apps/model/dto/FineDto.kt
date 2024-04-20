package br.com.apps.model.dto

import java.util.Date

data class FineDto(
    val masterUid: String? = null,
    var id: String? = null,
    val expenseId: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val date: Date? = null,
    val description: String? = null,
    val code: String? = null,
    val value: Double? = null
)