package br.com.apps.model.mapper

import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.PaymentRequestStatusType
import java.time.LocalDateTime
import java.util.Date

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

 fun Date.toLocalDateTime(): LocalDateTime {
    val instant = this.toInstant()
    return LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
}




