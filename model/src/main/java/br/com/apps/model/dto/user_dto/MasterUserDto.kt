package br.com.apps.model.dto.user_dto

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

    override fun validateForDataBaseInsertion() {}

}