package br.com.apps.model.dto

data class TruckDto(

    var id: String? = null,
    val masterUid: String? = null,
    val driverId: String? = null,

    val plate: String? = "",
    val color: String? = ""

)