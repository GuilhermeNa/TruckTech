package br.com.apps.model.dto.request.request

import java.util.Date

data class PaymentRequestDto(
    val masterUid: String? = null,
    var id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    val date: Date? = null,
    val requestNumber: Int? = 0,
    val status: String? = "",
    val itemsList: List<RequestItemDto>? = null

    ) {


}
