package br.com.apps.model.mapper

import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.PaymentRequestStatusType
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime

fun TravelRequestDto.toModel(): PaymentRequest {
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

fun PaymentRequest.toDto(): TravelRequestDto {
    return TravelRequestDto(
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




