package br.com.apps.model.model.employee

import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.enums.WorkRole
import br.com.apps.model.interfaces.ModelObjectInterface

/**
 * This abstract class represents a general employee within the system.
 *
 * @property masterUid Unique identifier for the master record associated with this employee.
 * @property id Unique identifier for the [Employee].
 * @property name Name of the employee.
 * @property type The role or type of work this employee performs.
 */
abstract class Employee(
    open val masterUid: String,
    open val id: String,
    open val name: String,
    open val type: WorkRole
) : ModelObjectInterface<EmployeeDto> {

    abstract override fun toDto(): EmployeeDto

}


