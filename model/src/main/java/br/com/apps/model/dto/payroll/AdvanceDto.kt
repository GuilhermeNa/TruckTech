package br.com.apps.model.dto.payroll

import br.com.apps.model.enums.AdvanceType
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
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

) : DtoObjectInterface<Advance> {

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

    override fun validateDataForDbInsertion() {}

    override fun toModel(): Advance {
        validateDataIntegrity()
        return Advance(
            masterUid = masterUid!!,
            id = id,
            travelId = travelId,
            employeeId = employeeId!!,
            date = date!!.toLocalDateTime(),
            value = BigDecimal(value!!),
            isPaid = isPaid!!,
            isApproved = isApproved!!,
            type = AdvanceType.valueOf(type!!)
        )
    }

}