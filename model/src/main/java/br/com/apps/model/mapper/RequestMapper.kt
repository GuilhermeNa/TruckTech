package br.com.apps.model.mapper

import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.PaymentRequestStatusType
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

fun PaymentRequestDto.toModel(): PaymentRequest {
    return PaymentRequest(
        masterUid = masterUid,
        id = id,
        driverId = driverId,
        truckId = truckId,

        encodedImage = encodedImage,
        requestNumber = requestNumber,
        date = date?.toLocalDateTime(),
        status = status?.let { status -> PaymentRequestStatusType.getType(status) }
    )
}

fun PaymentRequest.toDto(): PaymentRequestDto {
    return PaymentRequestDto(
        masterUid = masterUid,
        id = id,
        driverId = driverId,
        truckId = truckId,

        encodedImage = encodedImage,
        requestNumber = requestNumber,
        date = date?.toDate(),
        status = status?.description
    )
}




