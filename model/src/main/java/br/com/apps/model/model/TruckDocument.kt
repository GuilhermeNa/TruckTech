package br.com.apps.model.model

import java.io.Serializable
import java.time.LocalDateTime

data class TruckDocument(
    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val expenseId: String? = null,
    val labelId: String? = null,

    var name: String? = null,
    val urlImage: String? = null,
    val plate: String? = null,
    val expeditionDate: LocalDateTime? = null,
    val expirationDate: LocalDateTime? = null
): Serializable