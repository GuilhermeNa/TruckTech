package br.com.apps.model.dto.request.request

data class RequestItemDto(
    var id: String? = null,
    val labelId: String? = null,

    val kmMarking: Int? = 0,
    val value: Double? = 0.0,
    val type: String? = ""
)
