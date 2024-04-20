package br.com.apps.model.factory

import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.model.model.request.request.RequestItemType

object RequestItemDtoFactory {
    fun create(kmMarking: String? = null, value: String, labelId: String? = null, type: RequestItemType): RequestItem {
        return RequestItem(
            kmMarking = kmMarking?.toInt(),
            value = value.toBigDecimal(),
            type = type,
        )
    }

}