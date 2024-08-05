package br.com.apps.model.model.user

import br.com.apps.model.dto.user_dto.UserDto
import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.interfaces.ModelObjectInterface

abstract class User(

    open val masterUid: String? = null,

    open val email: String? = "",
    open val name: String? = "",
    open val orderCode: Int,
    open val orderNumber: Int,

): ModelObjectInterface<UserDto>

enum class AccessLevel(val description: String) {
    OPERATIONAL("OPERATIONAL"),
    TRAINEE("TRAINEE"),
    ADMIN_ASSISTANT("ADMIN_ASSISTANT"),
    MANAGER("MANAGER"),
    MASTER("MASTER");

    companion object {

        fun getType(s: String): AccessLevel {
            return when (s) {
                "OPERATIONAL" -> OPERATIONAL
                "TRAINEE" -> TRAINEE
                "ADMIN_ASSISTANT" -> ADMIN_ASSISTANT
                "MANAGER" -> MANAGER
                "MASTER" -> MASTER
                else -> throw InvalidTypeException("Invalid type for this string ($s)")
            }
        }

    }

}
