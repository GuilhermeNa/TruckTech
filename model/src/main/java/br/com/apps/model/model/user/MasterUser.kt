package br.com.apps.model.model.user

data class MasterUser(

    override val masterUid: String? = null,

    override val email: String? = "",
    override val name: String? = "",

    ) : User(
    masterUid = masterUid,
    email = email,
    name = name
)
