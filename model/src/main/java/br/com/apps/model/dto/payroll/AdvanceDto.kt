package br.com.apps.model.dto.payroll

import br.com.apps.model.enums.AdvanceType
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [Advance].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class AdvanceDto(
    var masterUid: String? = null,
    var id: String? = null,
    var travelId: String? = null,
    var employeeId: String? = null,

    var date: Date? = null,
    var value: Double? = null,
    var type: String? = null

) : DtoObjectInterface<Advance> {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            employeeId == null ||
            date == null ||
            value == null ||
            type == null
        ) throw CorruptedFileException("AdvanceDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            employeeId == null ||
            date == null ||
            value == null ||
            type == null
        ) throw InvalidForSavingException("AdvanceDto data is invalid: ($this)")
    }

    override fun toModel(): Advance {
        validateDataIntegrity()
        return Advance(
            masterUid = masterUid!!,
            id = id!!,
            travelId = travelId,
            employeeId = employeeId!!,
            date = date!!.toLocalDateTime(),
            value = BigDecimal(value!!).setScale(2),
            type = AdvanceType.valueOf(type!!)
        )
    }

}