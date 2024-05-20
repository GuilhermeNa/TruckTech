package br.com.apps.model.dto.user_dto

data class MasterUserDto(

    override var masterUid: String? = null,

    override val email: String? = null,
    override val name: String? = null,

    ) : UserDto(
    masterUid = masterUid,
    email = email,
    name = name
) {

    override fun validateFields(): Boolean {
       return true
    }

}