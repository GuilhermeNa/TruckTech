package br.com.apps.model.model.travel

import java.math.BigDecimal

data class Refuel(
    val id: String? = null,
    val truckId: String? = null,
    val travelId: String? = null,
    val costId: String? = null,

    val station: String? = null,
    val odometerMeasure: BigDecimal? = BigDecimal.ZERO,
    val valuePerLiter: BigDecimal? = BigDecimal.ZERO,
    val amountLiters: BigDecimal? = BigDecimal.ZERO
)