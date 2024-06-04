package br.com.apps.model.dto.request.request

import java.util.Date

data class PaymentRequestDto(
    val masterUid: String? = null,
    var id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val encodedImage: String? = null,
    val date: Date? = null,
    var requestNumber: Int? = null,
    val status: String? = null,
    val itemsList: List<RequestItemDto>? = null

) {

    fun validateFields(): Boolean {
        var isValid = true

        if (masterUid == null ||
            truckId == null ||
            driverId == null ||
            date == null ||
            requestNumber == null ||
            status == null
        ) {
            isValid = false
        }

        return isValid
    }

}
