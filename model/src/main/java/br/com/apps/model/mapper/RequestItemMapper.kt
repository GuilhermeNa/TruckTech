package br.com.apps.model.mapper

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.request.travel_requests.RequestItemType
import java.math.BigDecimal

fun RequestItemDto.toModel(): RequestItem {
    return RequestItem(
        id = this.id,
        labelId = this.labelId,
        requestId = this.requestId,
        kmMarking = this.kmMarking,
        type = this.type?.let { type -> RequestItemType.getType(type) },
        value = this.value?.let { BigDecimal.valueOf(it) }
    )
}

fun RequestItem.toDto(): RequestItemDto {
    return RequestItemDto(
        id = this.id,
        labelId = this.labelId,
        requestId = this.requestId,
        kmMarking = this.kmMarking,
        value = this.value?.toDouble(),
        type = this.type?.description
    )
}