package br.com.apps.model.dto.user_dto

import br.com.apps.model.exceptions.CorruptedFileException
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

) : UserDto(
    masterUid = masterUid,
    email = email,
    name = name,
    orderCode = orderCode,
    orderNumber = orderNumber
) {

    override fun validateDataIntegrity() {
        if (uid == null ||
            name == null ||
            email == null ||
            masterUid == null ||
            employeeId == null ||
            permission == null ||
            orderCode == null ||
            orderNumber == null
        ) throw CorruptedFileException("CommonUserDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {

    }

}