package br.com.apps.model.model.employee

data class Admin(

    override val masterUid: String? = null,
    override val id: String? = null,

    override val name: String? = "",
    override val type: EmployeeType? = null

) : Employee(
    masterUid = masterUid,
    id = id,
    name = name,
    type = type
)