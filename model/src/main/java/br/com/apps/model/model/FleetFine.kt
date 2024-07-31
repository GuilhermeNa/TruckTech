package br.com.apps.model.model

import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.fleet.Fleet
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Data class representing a fine or penalty associated with a [Fleet], such as
 * speeding tickets or other traffic violations.
 *
 * @property masterUid Unique identifier for the master record associated with this fine.
 * @property id Unique identifier for the [FleetFine].
 * @property fleetId Identifier for the truck associated with the [Fleet].
 * @property employeeId Identifier for the [Employee] associated with the fine.
 * @property date Date and time when the fine was issued.
 * @property description A description providing details about the fine.
 * @property code Code or reference number related to the fine.
 * @property value Monetary value of the fine.
 */
data class FleetFine(
    val masterUid: String,
    val id: String,
    val fleetId: String,
    val employeeId: String,
    val date: LocalDateTime,
    val description: String,
    val code: String,
    val value: BigDecimal
)