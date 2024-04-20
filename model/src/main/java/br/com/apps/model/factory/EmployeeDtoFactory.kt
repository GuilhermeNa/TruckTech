package br.com.apps.model.factory

import br.com.apps.model.dto.employee_dto.AdminEmployeeDto
import br.com.apps.model.dto.employee_dto.DriverEmployeeDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.employee.EmployeeType

object EmployeeDtoFactory {

    fun create(masterUid: String, name: String, position: String): EmployeeDto {
        return when (position) {
            EmployeeType.DRIVER.description -> createDriverEmployee(masterUid, name)
            EmployeeType.ADMIN.description -> createAdminEmployee(masterUid, name)
            else -> throw IllegalArgumentException()
        }
    }

    private fun createAdminEmployee(masterUid: String, name: String): AdminEmployeeDto {
        return AdminEmployeeDto(
            masterUid = masterUid,
            name = name,
            type = EmployeeType.ADMIN.description
        )
    }

    private fun createDriverEmployee(masterUid: String, name: String): DriverEmployeeDto {
        return DriverEmployeeDto(
            masterUid = masterUid,
            name = name,
            type = EmployeeType.DRIVER.description
        )
    }


}