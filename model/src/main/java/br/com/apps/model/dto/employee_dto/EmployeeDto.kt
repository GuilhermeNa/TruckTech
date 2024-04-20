package br.com.apps.model.dto.employee_dto

abstract class EmployeeDto(

    open val masterUid: String? = null,
    open var id: String? = null,

    open val name: String? = "",
    open val type: String? = null

)