package br.com.apps.model.model

import java.math.BigDecimal

data class Truck(

    val masterUid: String,
    val id: String? = null,
    val driverId: String,

    val plate: String,
    val color: String,
    val commissionPercentual: BigDecimal

)