package br.com.apps.trucktech.model.employee

abstract class OperationalEmployee(
    override val id: String?,
    override val name: String,
    override val userName: String,
    override val password: String,
    override val authorizationLevel: AuthorizationLevelType
    ) :
    Employee(
        id = id,
        name = name,
        userName = userName,
        password = password,
        authorizationLevel = authorizationLevel
    )
