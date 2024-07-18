package br.com.apps.model.dto.payroll

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException
import java.util.Date

data class AdvanceDto(
    var masterUid: String? = null,
    var id: String? = null,
    var travelId: String? = null,
    var employeeId: String? = null,

    var date: Date? = null,
    var value: Double? = null,
    @field:JvmField
    var isPaid: Boolean? = null,
    @field:JvmField
    var isApproved: Boolean? = null,
    var type: String? = null

) : DtoInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            employeeId == null ||
            date == null ||
            value == null ||
            isPaid == null ||
            isApproved == null ||
            type == null
        ) throw CorruptedFileException("AdvanceDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {}

}