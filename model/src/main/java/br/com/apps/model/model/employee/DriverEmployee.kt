package br.com.apps.model.model.employee

data class DriverEmployee(

    override val masterUid: String? = null,
    override val id: String? = null,
    var truckId: String? = null,

    override val name: String? = "",
    override val type: EmployeeType? = null

): Employee(
    masterUid = masterUid,
    id = id,
    name = name,
    type = type
)