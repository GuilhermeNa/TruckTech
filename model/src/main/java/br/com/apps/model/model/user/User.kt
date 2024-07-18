package br.com.apps.model.model.user

import br.com.apps.model.exceptions.InvalidTypeException

abstract class User(

    open val masterUid: String? = null,

    open val email: String? = "",
    open val name: String? = "",
    open val orderCode: Int,
    open val orderNumber: Int,
)

enum class PermissionLevelType(val description: String) {
    OPERATIONAL("OPERATIONAL"),
    TRAINEE("TRAINEE"),
    ADMIN_ASSISTANT("ADMIN_ASSISTANT"),
    MANAGER("MANAGER"),
    MASTER("MASTER");

    companion object {

        fun getType(s: String): PermissionLevelType {
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
