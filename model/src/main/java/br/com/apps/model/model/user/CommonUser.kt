package br.com.apps.model.model.user

data class CommonUser(

    override val masterUid: String? = null,
    val uid: String? = null,
    val employeeId: String? = null,

    override val email: String? = "",
    override val name: String? = "",
    val urlImage: String? = "",
    val permission: PermissionLevelType? = null

) : User(
    masterUid = masterUid,
    email = email,
    name = name
)