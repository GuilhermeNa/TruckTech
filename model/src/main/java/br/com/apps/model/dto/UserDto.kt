package br.com.apps.model.dto

import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.User

class UserDto(
    var masterUid: String? = null,
    val employeeId: String? = null,
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val lastRequest: Long? = null,
    val urlImage: String? = null,
    val permission: String? = null
) : DtoObjectInterface<User> {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            employeeId == null ||
            uid == null ||
            name == null ||
            email == null ||
            lastRequest == null ||
            permission == null
        ) throw CorruptedFileException("UserDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        TODO("Not yet implemented")
    }

    override fun toModel(): User {
        validateDataIntegrity()
        return User(
            masterUid = masterUid!!,
            employeeId = employeeId!!,
            uid = uid!!,
            name = name!!,
            email = email!!,
            lastRequest = lastRequest!!,
            urlImage = urlImage!!,
            permission = AccessLevel.valueOf(permission!!)
        )
    }

}