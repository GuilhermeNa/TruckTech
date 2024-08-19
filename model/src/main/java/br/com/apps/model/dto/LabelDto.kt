package br.com.apps.model.dto

import br.com.apps.model.enums.LabelCategory
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.label.Label

data class LabelDto(
    var masterUid: String? = null,
    var id: String? = null,
    var name: String? = null,
    var urlIcon: String? = null,
    var color: Int? = 0,
    var type: String? = null,
    @field:JvmField
    var isDefaultLabel: Boolean? = null,
    @field:JvmField
    var isOperational: Boolean? = null
) : DtoObjectInterface<Label> {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            name == null ||
            type == null ||
            isDefaultLabel == null ||
            isOperational == null
        ) throw CorruptedFileException("LabelDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {}

    override fun toModel(): Label {
        validateDataIntegrity()
        return Label(
            masterUid = this.masterUid!!,
            id = this.id!!,
            name = this.name!!,
            urlIcon = this.urlIcon,
            color = this.color,
            type = LabelCategory.valueOf(this.type!!),
            isDefaultLabel = this.isDefaultLabel!!,
            isOperational = this.isOperational!!
        )
    }

}