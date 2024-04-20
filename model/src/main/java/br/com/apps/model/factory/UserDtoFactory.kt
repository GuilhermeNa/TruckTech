package br.com.apps.model.factory

import br.com.apps.model.dto.user_dto.CommonUserDto
import br.com.apps.model.model.UserCredentials

object UserDtoFactory {

    fun createObject(credentials: UserCredentials, employeeId: String): CommonUserDto {
        return CommonUserDto(
            masterUid = credentials.masterUid,
            employeeId = employeeId,
            name = credentials.name,
            email = credentials.email,
            permission = credentials.permissionLevel
        )
    }

}