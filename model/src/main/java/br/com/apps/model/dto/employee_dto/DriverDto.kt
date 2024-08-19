package br.com.apps.model.dto.employee_dto

import br.com.apps.model.enums.WorkRole
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.model.employee.Driver

/**
 * Data Transfer Object (DTO) representing a [Driver].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class DriverDto(
    override val masterUid: String? = null,
    override var id: String? = null,
    override val name: String? = null,
    override val type: String? = null,

    var truckId: String? = null

) : EmployeeDto(masterUid = masterUid, id = id, name = name, type = type) {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            name == null ||
            type == null ||
            truckId == null
        ) throw CorruptedFileException("DriverDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            name == null ||
            type == null ||
            truckId == null
        ) throw InvalidForSavingException("DriverDto data is invalid: ($this)")
    }

    override fun toModel(): Driver {
        validateDataIntegrity()
        return Driver(
            masterUid = masterUid!!,
            id = id!!,
            truckId = truckId!!,
            name = name!!,
            type = WorkRole.valueOf(type!!)
        )
    }

}