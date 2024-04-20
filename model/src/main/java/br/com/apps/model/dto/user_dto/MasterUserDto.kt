package br.com.apps.model.dto.user_dto

data class MasterUserDto(

    override var masterUid: String? = null,

    override val email: String? = "",
    override val name: String? = "",

    ) : UserDto(
    masterUid = masterUid,
    email = email,
    name = name
)