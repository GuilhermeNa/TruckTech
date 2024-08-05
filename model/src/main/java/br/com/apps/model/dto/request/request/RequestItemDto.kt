package br.com.apps.model.dto.request.request

import br.com.apps.model.enums.RequestItemType
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.request.RequestItem
import java.math.BigDecimal

data class RequestItemDto(
    var id: String? = null,
    val labelId: String? = null,
    var requestId: String? = null,

    var docUrl: String? = null,
    val kmMarking: Int? = null,
    var value: Double? = null,
    var type: String? = null
) : DtoObjectInterface<RequestItem> {

    override fun validateDataIntegrity() {
        if (id == null ||
            requestId == null ||
            value == null ||
            type == null
        ) throw CorruptedFileException("RequestItemDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (requestId == null ||
            value == null ||
            type == null
        ) throw InvalidForSavingException("RequestItemDto data is invalid: ($this)")
    }

    override fun toModel(): RequestItem {
        validateDataIntegrity()
        return RequestItem(
            id = id,
            labelId = labelId,
            requestId = requestId!!,
            docUrl = docUrl,
            kmMarking = kmMarking,
            type = RequestItemType.getType(type!!),
            value = BigDecimal.valueOf(value!!)
        )
    }

}
