package br.com.apps.model.dto.user_dto

import br.com.apps.model.model.user.PermissionLevelType

data class CommonUserDto(
    var uid: String? = null,
    override var masterUid: String? = null,
    var employeeId: String? = null,

    override val orderCode: Int? = null,
    override val orderNumber: Int? = null,
    override val email: String? = null,
    override val name: String? = null,
    val urlImage: String? = null,
    val permission: PermissionLevelType? = null

): UserDto(
    masterUid = masterUid,
    email = email,
    name = name,
    orderCode = orderCode,
    orderNumber = orderNumber
) {

    override fun validateFields(): Boolean {
        var isValid = true

        if(uid == null) isValid = false
        if(name == null) isValid = false
        if(email == null) isValid = false
        if(masterUid == null) isValid = false
        if(employeeId == null) isValid = false
        if(permission == null) isValid = false
        if(orderCode == null) isValid = false
        if(orderNumber == null) isValid = false

        return isValid
    }

}