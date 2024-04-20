package br.com.apps.model.mapper

import br.com.apps.model.dto.user_dto.CommonUserDto
import br.com.apps.model.dto.user_dto.MasterUserDto
import br.com.apps.model.dto.user_dto.UserDto
import br.com.apps.model.model.UserCredentials
import br.com.apps.model.model.user.CommonUser
import br.com.apps.model.model.user.MasterUser
import br.com.apps.model.model.user.User

class UserMapper {

    companion object {

        fun toDto(credentials: UserCredentials): UserDto {
            return CommonUserDto(
                masterUid = credentials.masterUid,
                email = credentials.email,
                name = credentials.name,
                permission = credentials.permissionLevel
            )
        }

        fun toModel(userDto: UserDto): User {
            return when(userDto) {
                is MasterUserDto -> MasterUser(
                    masterUid = userDto.masterUid,
                    email = userDto.email,
                    name = userDto.name,
                )
                is CommonUserDto -> CommonUser(
                    masterUid = userDto.masterUid,
                    uid = userDto.uid,
                    employeeId = userDto.employeeId,
                    email = userDto.email,
                    name = userDto.name,
                    urlImage= userDto.urlImage,
                    permission = userDto.permission
                )

                else -> throw IllegalArgumentException()
            }
        }

    }

}