package br.com.apps.trucktech.model.employee

abstract class Employee(

    open val id: String?,
    open val password: String,
    open val name: String,
    open val userName: String,
    open val authorizationLevel: AuthorizationLevelType

) {

}

enum class AuthorizationLevelType {
    OPERATIONAL, TRAINEE, ADMIN_ASSISTANT, MANAGER, ADMIN
}