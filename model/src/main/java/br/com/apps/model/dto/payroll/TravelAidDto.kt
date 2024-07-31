package br.com.apps.model.dto.payroll

import br.com.apps.model.dto.DtoObjectsInterface
import br.com.apps.model.exceptions.CorruptedFileException
import java.util.Date

data class TravelAidDto(
    var masterUid: String? = null,
    var id: String? = null,
    var driverId: String? = null,
    var travelId: String? = null,
    var date: Date? = null,
    var value: Double? = null,
    @field:JvmField
    var isPaid: Boolean? = null
) : DtoObjectsInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            driverId == null ||
            travelId == null ||
            date == null ||
            value == null ||
            isPaid == null
        ) throw CorruptedFileException("TravelAidDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {}

}