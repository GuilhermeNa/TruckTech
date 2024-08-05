package br.com.apps.model.dto.request.request

import br.com.apps.model.enums.PaymentRequestStatusType
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.request.PaymentRequest
import br.com.apps.model.util.toLocalDateTime
import java.util.Date

data class TravelRequestDto(
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var driverId: String? = null,
    val encodedImage: String? = null,
    var date: Date? = null,
    var requestNumber: Int? = null,
    var status: String? = null
) : DtoObjectInterface<PaymentRequest> {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            truckId == null ||
            driverId == null ||
            date == null ||
            requestNumber == null ||
            status == null
        ) throw CorruptedFileException("TravelRequestDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            truckId == null ||
            driverId == null ||
            date == null ||
            requestNumber == null ||
            status == null
        ) throw InvalidForSavingException("TravelRequestDto data is invalid: ($this)")
    }

    override fun toModel(): PaymentRequest {
        validateDataIntegrity()
        return PaymentRequest(
            masterUid = masterUid!!,
            id = id,
            driverId = driverId,
            truckId = truckId,
            encodedImage = encodedImage,
            requestNumber = requestNumber!!,
            date = date!!.toLocalDateTime(),
            status = PaymentRequestStatusType.valueOf(status!!)
        )
    }

}
