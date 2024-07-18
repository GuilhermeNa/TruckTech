package br.com.apps.model.mapper

import br.com.apps.model.dto.user_dto.CommonUserDto
import br.com.apps.model.dto.user_dto.MasterUserDto
import br.com.apps.model.dto.user_dto.UserDto
import br.com.apps.model.model.UserCredentials
import br.com.apps.model.model.user.CommonUser
import br.com.apps.model.model.user.MasterUser
import br.com.apps.model.model.user.User

fun User.toDto(credentials: UserCredentials): UserDto {
    return CommonUserDto(
        masterUid = credentials.masterUid,
        email = credentials.email,
        name = credentials.name,
        permission = credentials.permissionLevel
    )
}

fun UserDto.toModel(): User {
    return when (this) {
        is MasterUserDto -> mapMasterUser(this)
        is CommonUserDto -> mapCommonUser(this)
        else -> throw IllegalArgumentException()
    }
}

private fun mapMasterUser(dto: MasterUserDto): MasterUser {
    dto.validateDataIntegrity()
    return MasterUser(
        masterUid = dto.masterUid!!,
        email = dto.email!!,
        name = dto.name!!,
        orderCode = dto.orderCode!!,
        orderNumber = dto.orderNumber!!
    )
}

private fun mapCommonUser(dto: CommonUserDto): CommonUser {
    dto.validateDataIntegrity()
    return CommonUser(
        masterUid = dto.masterUid!!,
        uid = dto.uid!!,
        employeeId = dto.employeeId!!,
        orderCode = dto.orderCode!!,
        orderNumber = dto.orderNumber!!,
        email = dto.email!!,
        name = dto.name!!,
        urlImage = dto.urlImage,
        permission = dto.permission!!
    )
}
