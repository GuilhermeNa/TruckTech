package br.com.apps.model.dto

import java.util.Date

data class DocumentDto(

    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val expenseId: String? = null,
    val labelId: String? = null,

    val urlImage: String? = "",
    val name: String? = "",
    val plate: String? = "",
    val expeditionDate: Date? = null,
    val expirationDate: Date? = null

) {



}