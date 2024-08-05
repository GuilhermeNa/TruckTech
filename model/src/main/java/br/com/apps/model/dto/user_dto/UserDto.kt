package br.com.apps.model.dto.user_dto

import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.user.User

abstract class UserDto(
    open var masterUid: String? = null,
    open val email: String? = null,
    open val name: String? = null,
    open val orderCode: Int? = null,
    open val orderNumber: Int? = null,
) : DtoObjectInterface<User>