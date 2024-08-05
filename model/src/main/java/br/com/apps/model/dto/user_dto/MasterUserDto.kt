package br.com.apps.model.dto.user_dto

import br.com.apps.model.model.user.MasterUser

data class MasterUserDto(

    override var masterUid: String? = null,

    override val email: String? = null,
    override val name: String? = null,
    override val orderCode: Int? = null,
    override val orderNumber: Int? = null,

    ) : UserDto(
    masterUid = masterUid,
    email = email,
    name = name,
    orderCode = orderCode,
    orderNumber = orderNumber
) {

    override fun validateDataIntegrity() {}

    override fun validateDataForDbInsertion() {}

    override fun toModel(): MasterUser {
        validateDataIntegrity()
        return MasterUser(
            masterUid = masterUid!!,
            email = email!!,
            name = name!!,
            orderCode = orderCode!!,
            orderNumber = orderNumber!!
        )
    }

}