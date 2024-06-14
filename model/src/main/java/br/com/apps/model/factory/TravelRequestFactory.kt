package br.com.apps.model.factory

import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

object TravelRequestFactory {

    fun createModel(): PaymentRequest? {

        return null
    }

    fun create(
        masterUid: String?,
        truckId: String?,
        driverId: String?,
        requestNumber: Int?,
        date: LocalDateTime?,
        status: String?
    ): TravelRequestDto {

        return TravelRequestDto(
            masterUid = masterUid,
            truckId = truckId,
            driverId = driverId,
            requestNumber = requestNumber,
            date = date?.toDate(),
            status = status
        )

    }

    private fun LocalDateTime.toDate(): Date {
        val zoneId = ZoneId.systemDefault()
        return Date.from(this.atZone(zoneId).toInstant())
    }

}