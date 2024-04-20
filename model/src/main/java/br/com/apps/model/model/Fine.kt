package br.com.apps.model.model

import java.time.LocalDateTime

data class Fine(

    val uid: String? = null,
    val id: String? = null,
    val expenseId: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val date: LocalDateTime? = null,
    val description: String = "",
    val code: String = ""

)