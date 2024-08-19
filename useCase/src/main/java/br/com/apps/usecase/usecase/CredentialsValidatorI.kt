package br.com.apps.usecase.usecase

import br.com.apps.model.enums.AccessLevel

interface CredentialsValidatorI<T> {

    fun validatePermission(permission: AccessLevel, t: T)

}