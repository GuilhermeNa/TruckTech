package br.com.apps.model.dto

import br.com.apps.model.exceptions.CorruptedFileException

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
) : DtoObjectsInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            name == null ||
            type == null ||
            isDefaultLabel == null ||
            isOperational == null
        ) throw CorruptedFileException("LabelDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {}

}