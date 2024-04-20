package br.com.apps.model.mapper

import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.PaymentRequestStatusType
import br.com.apps.model.toLocalDateTime

fun PaymentRequestDto.toModel(): PaymentRequest {
    return PaymentRequest(
        masterUid = this.masterUid,
        id = this.id,
        driverId = this.driverId,
        truckId = this.truckId,
        requestNumber = this.requestNumber,
        date = this.date?.toLocalDateTime(),
        status = this.status?.let { status -> PaymentRequestStatusType.getType(status) }
    )
}




