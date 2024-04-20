package br.com.apps.model.dto

data class LabelDto(

    val uid: String? = null,
    var id: String? = null,

    var name: String? = "",
    var icon: Int? = 0,
    var color: Int? = 0,
    val type: String? = "",
    val isDefaultLabel: Boolean? = null

)