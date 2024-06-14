package br.com.apps.usecase

import br.com.apps.model.model.user.PermissionLevelType

interface CredentialsValidatorI<T> {

    fun validatePermission(permission: PermissionLevelType, t: T)

}