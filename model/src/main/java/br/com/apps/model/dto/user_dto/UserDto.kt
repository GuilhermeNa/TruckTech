package br.com.apps.model.dto.user_dto

abstract class UserDto(

    open var masterUid: String? = null,

    open val email: String? = "",
    open val name: String? = "",

    )