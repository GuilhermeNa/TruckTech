package br.com.apps.model.mapper

import br.com.apps.model.dto.user_dto.CommonUserDto
import br.com.apps.model.dto.user_dto.MasterUserDto
import br.com.apps.model.dto.user_dto.UserDto
import br.com.apps.model.exceptions.CorruptedFileException
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
    if (dto.validateFields()) {
        return MasterUser(
            masterUid = dto.masterUid!!,
            email = dto.email!!,
            name = dto.name!!,
        )
    }
    throw CorruptedFileException("UserMapper, mapMasterUser ($dto)")
}

private fun mapCommonUser(dto: CommonUserDto): CommonUser {
    if (dto.validateFields()) {
        return CommonUser(
            masterUid = dto.masterUid!!,
            uid = dto.uid!!,
            employeeId = dto.employeeId!!,
            email = dto.email!!,
            name = dto.name!!,
            urlImage = dto.urlImage,
            permission = dto.permission!!
        )
    }

    throw CorruptedFileException("UserMapper, mapCommonUser ($dto)")
}