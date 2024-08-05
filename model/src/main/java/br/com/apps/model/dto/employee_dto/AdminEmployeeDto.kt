package br.com.apps.model.dto.employee_dto

import br.com.apps.model.enums.WorkRole
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.employee.Admin

data class AdminEmployeeDto(

    override val masterUid: String? = null,
    override var id: String? = null,

    override val name: String? = "",
    override val type: String? = ""

): EmployeeDto(
    masterUid = masterUid,
    id = id,
    name = name
), DtoObjectInterface<Admin> {

    override fun validateDataIntegrity() {}

    override fun validateDataForDbInsertion() {
        TODO("Not yet implemented")
    }

    override fun toModel(): Admin {
        validateDataIntegrity()
        return Admin(
            masterUid = masterUid,
            id = id,
            name = name,
            type = WorkRole.valueOf(type!!)
        )
    }

}

