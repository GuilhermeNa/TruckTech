package br.com.apps.model.dto

data class LabelDto(
    val uid: String? = null,
    var id: String? = null,

    var name: String? = null,
    var urlIcon: String? = null,
    var color: Int? = 0,
    val type: String? = null,
    @field:JvmField
    val isDefaultLabel: Boolean? = null,
    @field:JvmField
    val isOperational: Boolean? = null

)