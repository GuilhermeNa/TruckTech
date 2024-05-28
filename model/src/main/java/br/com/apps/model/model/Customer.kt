package br.com.apps.model.model

data class Customer(
    val masterUid: String,
    val id: String? = null,
    val cnpj: String,
    val name: String

)