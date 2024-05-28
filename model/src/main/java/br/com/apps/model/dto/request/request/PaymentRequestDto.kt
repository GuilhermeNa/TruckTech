package br.com.apps.model.dto.request.request

import java.util.Date

data class PaymentRequestDto(
    val masterUid: String? = null,
    var id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val encodedImage: String? = null,
    val date: Date? = null,
    val requestNumber: Int? = null,
    val status: String? = null,
    val itemsList: List<RequestItemDto>? = null

    ) {


}
