package br.com.apps.model.model.user

import br.com.apps.model.dto.user_dto.UserDto

data class CommonUser(

    override val masterUid: String,
    val uid: String,
    val employeeId: String,

    override val orderCode: Int,
    override val orderNumber: Int,
    override val email: String,
    override val name: String,
    val urlImage: String? = "",
    val permission: AccessLevel

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