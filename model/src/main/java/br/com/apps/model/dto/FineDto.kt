package br.com.apps.model.dto

import java.util.Date

data class FineDto(

    val uid: String? = null,
    val id: String? = null,
    val expenseId: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val date: Date? = null,
    val description: String? = "",
    val code: String? = ""

)