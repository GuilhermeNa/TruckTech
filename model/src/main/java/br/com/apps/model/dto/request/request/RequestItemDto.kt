package br.com.apps.model.dto.request.request

data class RequestItemDto(
    var id: String? = null,
    val labelId: String? = null,
    var requestId: String? = null,

    val docUrl: String? = null,
    val kmMarking: Int? = null,
    val value: Double? = null,
    var type: String? = null
) {

    fun validateFields(): Boolean {
        var isValid = true

        if (
            requestId == null ||
            value == null ||
            type == null
        ) {
            isValid = false
        }

        return isValid
    }

}
