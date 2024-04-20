package br.com.apps.model.dto.employee_dto

data class AdminEmployeeDto(

    override val masterUid: String? = null,
    override var id: String? = null,


    override val name: String? = "",
    override val type: String? = ""

): EmployeeDto(
    masterUid = masterUid,
    id = id,
    name = name
)

