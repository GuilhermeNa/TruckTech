package br.com.apps.model.dto.employee_dto

import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.employee.Employee

/**
 * Data Transfer Object (DTO) representing a [Employee].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
abstract class EmployeeDto(
    open val masterUid: String? = null,
    open var id: String? = null,
    open val name: String? = null,
    open val type: String? = null

): DtoObjectInterface<Employee> {

    abstract override fun toModel(): Employee

}