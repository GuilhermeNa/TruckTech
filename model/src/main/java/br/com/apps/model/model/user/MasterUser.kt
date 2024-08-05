package br.com.apps.model.model.user

import br.com.apps.model.dto.user_dto.UserDto

data class MasterUser(
    override val masterUid: String? = null,
    override val email: String? = null,
    override val name: String? = null,
    override val orderCode: Int,
    override val orderNumber: Int
) : User(
    masterUid = masterUid,
    email = email,
    name = name,
    orderCode = orderCode,
    orderNumber = orderNumber
) {

    override fun toDto(): UserDto {
        TODO("Not yet implemented")
    }

}
