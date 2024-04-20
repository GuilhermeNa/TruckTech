package br.com.apps.model.model

import java.time.LocalDateTime

data class Document(

    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val expenseId: String? = null,
    val labelId: String? = null,

    var name: String? = "",
    val urlImage: String? = "",
    val plate: String? = "",
    val expeditionDate: LocalDateTime? = null,
    val expirationDate: LocalDateTime? = null

) {

}