package br.com.apps.model.dto

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.FleetFine
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
) : DtoObjectsInterface {

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

    override fun validateForDataBaseInsertion() {
        TODO("Not yet implemented")
    }

}