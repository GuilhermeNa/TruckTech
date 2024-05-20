package br.com.apps.model.model

data class Truck(

    val masterUid: String,
    val id: String? = null,
    val driverId: String,

    val plate: String,
    val color: String

)