package br.com.apps.model.dto.request

import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.interfaces.AccessPermissionInterface
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.request.Item
import br.com.apps.model.util.ACCESS_DENIED
import br.com.apps.model.util.toLocalDateTime
import java.util.Date

data class ItemDto(
    var masterUid: String? = null,
    var id: String? = null,
    var parentId: String? = null,
    val value: Double? = null,
    val description: String? = null,
    var urlImage: String? = null,
    var isValid: Boolean? = null,
    var isUpdating: Boolean? = null,
    var date: Date? = null
) : DtoObjectInterface<Item>,
    AccessPermissionInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            parentId == null ||
            value == null ||
            description == null ||
            isValid == null ||
            isUpdating == null ||
            date == null
        ) throw CorruptedFileException("ItemDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            parentId == null ||
            value == null ||
            description == null ||
            isValid == null ||
            isUpdating == null ||
            date == null
        ) throw InvalidForSavingException("ItemDto data is invalid: ($this)")
    }

    override fun toModel(): Item {
        validateDataIntegrity()
        return Item(
            masterUid = masterUid!!,
            id = id!!,
            parentId = parentId!!,
            value = value!!.toBigDecimal().setScale(2),
            description = description!!,
            isValid = isValid!!,
            urlImage = urlImage,
            isUpdating = isUpdating!!,
            date = date!!.toLocalDateTime()
        )
    }

    override fun validateWriteAccess(access: AccessLevel?) {
        if (access == null) throw NullPointerException()

        isValid?.let {
            if (it) throw AccessLevelException(ACCESS_DENIED)
        } ?: throw NullPointerException()
    }

    override fun validateReadAccess() {
        TODO("Not yet implemented")
    }

}