package br.com.apps.model.dto.request.request

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException

data class RequestItemDto(
    var id: String? = null,
    val labelId: String? = null,
    var requestId: String? = null,

    var docUrl: String? = null,
    val kmMarking: Int? = null,
    var value: Double? = null,
    var type: String? = null
) : DtoInterface {

    override fun validateDataIntegrity() {
        if (id == null ||
            requestId == null ||
            value == null ||
            type == null
        ) throw CorruptedFileException("RequestItemDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {
        if (requestId == null ||
            value == null ||
            type == null
        ) throw InvalidForSavingException("RequestItemDto data is invalid: ($this)")
    }

}
