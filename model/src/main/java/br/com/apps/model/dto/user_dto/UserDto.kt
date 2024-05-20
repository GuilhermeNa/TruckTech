package br.com.apps.model.dto.user_dto

abstract class UserDto(
    open var masterUid: String? = null,

    open val email: String? = null,
    open val name: String? = null,

    ) {

    abstract fun validateFields(): Boolean

}