package br.com.apps.model.model.user

data class MasterUser(

    override val masterUid: String? = null,

    override val email: String? = null,
    override val name: String? = null,

    ) : User(
    masterUid = masterUid,
    email = email,
    name = name
)
