package br.com.apps.model.model

import br.com.apps.model.enums.AccessLevel

data class UserCredentials(

    var masterUid: String? = null,
    val name: String,
    val email: String,
    val password: String,
    val permissionLevel: AccessLevel

)

