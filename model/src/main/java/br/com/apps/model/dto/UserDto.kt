package br.com.apps.model.dto

import br.com.apps.model.model.user.PermissionLevelType

data class UserDto(

    var uid: String? = "",
    val email: String? = "",
    val name: String? = "",
    val permission: PermissionLevelType? = null

    )