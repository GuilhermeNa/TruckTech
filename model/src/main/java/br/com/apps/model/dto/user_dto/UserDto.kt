package br.com.apps.model.dto.user_dto

import br.com.apps.model.dto.DtoObjectsInterface

abstract class UserDto(
    open var masterUid: String? = null,
    open val email: String? = null,
    open val name: String? = null,
    open val orderCode: Int? = null,
    open val orderNumber: Int? = null,
) : DtoObjectsInterface