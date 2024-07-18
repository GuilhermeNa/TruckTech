package br.com.apps.model.dto.request.request

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
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
) : DtoInterface {

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

    override fun validateForDataBaseInsertion() {
        if (masterUid == null ||
            truckId == null ||
            driverId == null ||
            date == null ||
            requestNumber == null ||
            status == null
        ) throw InvalidForSavingException("TravelRequestDto data is invalid: ($this)")
    }

}
