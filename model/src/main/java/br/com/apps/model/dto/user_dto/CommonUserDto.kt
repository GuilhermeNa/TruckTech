package br.com.apps.model.dto.user_dto

import br.com.apps.model.model.user.PermissionLevelType

data class CommonUserDto(

    override var masterUid: String? = null,
    var uid: String? = null,
    var employeeId: String? = null,

    override val email: String? = "",
    override val name: String? = "",
    val urlImage: String? = "",
    val permission: PermissionLevelType? = null

): UserDto(
    masterUid = masterUid,
    email = email,
    name = name
)