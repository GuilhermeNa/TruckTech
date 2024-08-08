package br.com.apps.model.model.employee

import br.com.apps.model.dto.employee_dto.DriverDto
import br.com.apps.model.enums.WorkRole
import br.com.apps.model.model.fleet.Truck

/**
 * This class represents an employee who operates a truck and is responsible for transporting cargo.
 *
 * Notes:
 * * A driver is a specific type of [Employee] with additional details related to truck operations.
 *
 * @property masterUid Unique identifier for the master record associated with this driver.
 * @property id Unique identifier for the [Driver].
 * @property name Name of the driver.
 * @property type The role or type of work this driver performs.
 * @property truckId Identifier for the [Truck] that the driver operates.
 */
data class Driver(
    override val masterUid: String,
    override val id: String,
    override val name: String,
    override val type: WorkRole,

    var truckId: String

) : Employee(masterUid = masterUid, id = id, name = name, type = type) {

    override fun toDto() = DriverDto(
        masterUid = masterUid,
        id = id,
        name = name,
        type = type.name,
        truckId = truckId
    )

}