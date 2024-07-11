package br.com.apps.model.dto

import java.util.Date

data class TruckDocumentDto(
    val masterUid: String? = null,
    var id: String? = null,
    val truckId: String? = null,
    val expenseId: String? = null,
    val labelId: String? = null,

    val name: String? = null,
    val urlImage: String? = null,
    val plate: String? = null,
    val expeditionDate: Date? = null,
    val expirationDate: Date? = null
)