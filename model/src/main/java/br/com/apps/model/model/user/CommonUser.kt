package br.com.apps.model.model.user

data class CommonUser(

    override val masterUid: String,
    val uid: String,
    val employeeId: String,

    override val orderCode: Int,
    override val orderNumber: Int,
    override val email: String,
    override val name: String,
    val urlImage: String? = "",
    val permission: PermissionLevelType

) : User(
    masterUid = masterUid,
    email = email,
    name = name,
    orderCode = orderCode,
    orderNumber = orderNumber
)