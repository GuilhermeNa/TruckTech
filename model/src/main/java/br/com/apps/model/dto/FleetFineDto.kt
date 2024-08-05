package br.com.apps.model.dto

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.FleetFine
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [FleetFine].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class FleetFineDto(
    // Ids
    val masterUid: String? = null,
    var id: String? = null,
    val fleetId: String? = null,
    val employeeId: String? = null,

    // Others
    val date: Date? = null,
    val description: String? = null,
    val code: String? = null,
    val value: Double? = null
) : DtoObjectInterface<FleetFine> {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            fleetId == null ||
            employeeId == null ||
            date == null ||
            description == null ||
            code == null ||
            value == null
        ) throw CorruptedFileException("FleetFineDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        TODO("Not yet implemented")
    }

    override fun toModel(): FleetFine {
        validateDataIntegrity()
        return FleetFine(
            masterUid = masterUid!!,
            id = id!!,
            fleetId = fleetId!!,
            employeeId = employeeId!!,
            date = date?.toLocalDateTime()!!,
            description = description!!,
            code = code!!,
            value = BigDecimal(value!!)
        )
    }

}