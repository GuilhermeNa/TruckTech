package br.com.apps.model.dto.employee_dto

import br.com.apps.model.enums.WorkRole
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.employee.Driver

data class DriverEmployeeDto(

    override val masterUid: String? = null,
    override var id: String? = null,
    var truckId: String? = null,

    override val name: String? = "",
    override val type: String? = ""

): EmployeeDto(
    masterUid = masterUid,
    id = id,
    name = name
), DtoObjectInterface<Driver> {

    override fun validateDataIntegrity() {}

    override fun validateDataForDbInsertion() {
        TODO("Not yet implemented")
    }

    override fun toModel(): Driver {
        validateDataIntegrity()
        return Driver(
            masterUid = masterUid,
            id = id,
            truckId = truckId,
            name = name,
            type = WorkRole.valueOf(type!!)
        )
    }

}