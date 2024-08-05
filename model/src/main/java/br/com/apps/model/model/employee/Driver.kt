package br.com.apps.model.model.employee

import br.com.apps.model.enums.WorkRole

data class Driver(

    override val masterUid: String? = null,
    override val id: String? = null,
    var truckId: String? = null,

    override val name: String? = "",
    override val type: WorkRole? = null

): Employee(
    masterUid = masterUid,
    id = id,
    name = name,
    type = type
) {
    override fun toDto(): Employee {
        TODO("Not yet implemented")
    }

}