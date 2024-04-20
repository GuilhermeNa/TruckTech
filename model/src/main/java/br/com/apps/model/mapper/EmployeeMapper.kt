package br.com.apps.model.mapper

import android.util.Log
import br.com.apps.model.dto.employee_dto.AdminEmployeeDto
import br.com.apps.model.dto.employee_dto.DriverEmployeeDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.employee.AdminEmployee
import br.com.apps.model.model.employee.DriverEmployee
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

        private fun getAdminEmployee(adminEmployee: EmployeeDto): AdminEmployee {
            val admin = adminEmployee as? AdminEmployeeDto
            admin?.let {
                return AdminEmployee(
                    masterUid = it.masterUid,
                    id = it.id,
                    name = it.name,
                    type = EmployeeType.getType("ADMIN")
                )
            }  ?: Log.e("error", "EmployeeMapper -> getAdminEmployee: Null Object")
            throw NullPointerException()
        }

        private fun getDriverEmployee(driverDto: EmployeeDto): DriverEmployee {
            val driver = driverDto as? DriverEmployeeDto
            driver?.let {
                return DriverEmployee(
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