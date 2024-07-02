package br.com.apps.model.mapper

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.request.travel_requests.RequestItemType
import java.math.BigDecimal

fun RequestItemDto.toModel(): RequestItem {
    if(this.validateFields()) {
        return RequestItem(
            id = this.id,
            labelId = this.labelId,
            requestId = this.requestId!!,
            docUrl = this.docUrl,
            kmMarking = this.kmMarking,
            type = RequestItemType.getType(this.type!!),
            value = BigDecimal.valueOf(value!!)
        )
    }

    throw CorruptedFileException("RequestItemMapper, toModel ($this)")
}

fun RequestItem.toDto(): RequestItemDto {
    return RequestItemDto(
        id = this.id,
        labelId = this.labelId,
        requestId = this.requestId,
        docUrl = this.docUrl,
        kmMarking = this.kmMarking,
        value = this.value.toDouble(),
        type = this.type.description
    )
}