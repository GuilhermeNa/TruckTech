package br.com.apps.model.model

import br.com.apps.model.dto.UserDto
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.interfaces.ModelObjectInterface

class User(
    val masterUid: String,
    val uid: String,
    val employeeId: String,
    val name: String,
    val email: String,
    val urlImage: String,
    val lastRequest: Long,
    val permission: AccessLevel
): ModelObjectInterface<UserDto> {

    override fun toDto(): UserDto {
        return UserDto(
            masterUid = masterUid,
            employeeId = employeeId,
            uid = uid,
            name = name,
            email = email,
            lastRequest = lastRequest,
            urlImage = urlImage,
            permission = permission.name
        )
    }

}


