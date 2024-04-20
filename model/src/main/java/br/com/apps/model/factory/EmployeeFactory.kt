package br.com.apps.model.factory

import br.com.apps.model.dto.employee_dto.AdminEmployeeDto
import br.com.apps.model.dto.employee_dto.DriverEmployeeDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.employee.EmployeeType

object EmployeeFactory {

    fun createEmployee(employeeDto: EmployeeDto, type: EmployeeType): EmployeeDto {
        return when (type) {
            EmployeeType.DRIVER -> createDriverEmployee(employeeDto)
            EmployeeType.ADMIN -> createAdminEmployee(employeeDto)
        }
    }

    private fun createAdminEmployee(employeeDto: EmployeeDto): AdminEmployeeDto {
        TODO("Not yet implemented")
    }

    private fun createDriverEmployee(employeeDto: EmployeeDto): DriverEmployeeDto {
        TODO("Not yet implemented")
    }


}