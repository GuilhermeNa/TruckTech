package br.com.apps.model.dto.request

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.request.Item

data class ItemDto(
    val masterUid: String? = null,
    var id: String? = null,
    val parentId: String? = null,
    val value: Double? = null,
    val description: String? = null,
    val urlImage: String? = null,
    val isValid: Boolean? = null
): DtoObjectInterface<Item> {

    override fun validateDataIntegrity() {
        if(masterUid == null ||
            id == null ||
            parentId == null ||
            value == null ||
            description == null ||
            isValid == null
        ) throw CorruptedFileException("ItemDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if(masterUid == null ||
            parentId == null ||
            value == null ||
            description == null ||
            isValid == null
        ) throw InvalidForSavingException("ItemDto data is invalid: ($this)")
    }

    override fun toModel(): Item {
        validateDataIntegrity()
        return Item(
            masterUid = masterUid!!,
            id = id!!,
            parentId = parentId!!,
            value = value!!.toBigDecimal(),
            description = description!!,
            urlImage = urlImage,
            isValid = isValid!!
        )
    }

}