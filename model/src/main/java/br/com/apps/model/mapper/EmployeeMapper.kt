package br.com.apps.model.mapper

import android.util.Log
import br.com.apps.model.dto.employee_dto.AdminEmployeeDto
import br.com.apps.model.dto.employee_dto.DriverEmployeeDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.employee.Admin
import br.com.apps.model.model.employee.Driver
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.employee.EmployeeType

class EmployeeMapper {

    companion object {

        fun EmployeeDto.toModel(): Employee {
            return when (this.type) {
                "DRIVER" -> getDriverEmployee(this)
                "ADMIN" -> getAdminEmployee(this)
                else -> throw IllegalArgumentException()
            }
        }

        private fun getAdminEmployee(adminEmployee: EmployeeDto): Admin {
            val admin = adminEmployee as? AdminEmployeeDto
            admin?.let {
                return Admin(
                    masterUid = it.masterUid,
                    id = it.id,
                    name = it.name,
                    type = EmployeeType.getType("ADMIN")
                )
            }  ?: Log.e("error", "EmployeeMapper -> getAdminEmployee: Null Object")
            throw NullPointerException()
        }

        private fun getDriverEmployee(driverDto: EmployeeDto): Driver {
            val driver = driverDto as? DriverEmployeeDto
            driver?.let {
                return Driver(
                    masterUid = it.masterUid,
                    id = it.id,
                    truckId = it.truckId,
                    name = it.name,
                    type = EmployeeType.getType("DRIVER")
                )
            } ?: Log.e("error", "EmployeeMapper -> getDriverEmployee: Null Object")
            throw NullPointerException()
        }

    }
}