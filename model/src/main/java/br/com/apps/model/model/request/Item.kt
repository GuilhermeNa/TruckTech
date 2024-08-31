package br.com.apps.model.model.request

import br.com.apps.model.dto.request.ItemDto
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

data class Item(
    val masterUid: String,
    val id: String,
    val parentId: String,
    val value: BigDecimal,
    val description: String,
    val isValid: Boolean,
    val urlImage: String? = null,
    val isUpdating: Boolean,
    val date: LocalDateTime
): ModelObjectInterface<ItemDto> {

    override fun toDto(): ItemDto = ItemDto(
        masterUid = masterUid,
        id = id,
        parentId = parentId,
        value = value.toDouble(),
        description = description,
        isValid = isValid,
        urlImage = urlImage,
        isUpdating = isUpdating,
        date = date.toDate()
    )

}