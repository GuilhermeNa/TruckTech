package br.com.apps.model.dto.travel

data class RefuelDto(
    val id: String? = null,
    val truckId: String? = null,
    val travelId: String? = null,
    val costId: String? = null,

    val station: String? = null,
    val odometerMeasure: Double? = null,
    val valuePerLiter: Double? = null,
    val amountLiters: Double? = null
)