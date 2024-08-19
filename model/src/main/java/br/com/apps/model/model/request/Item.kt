package br.com.apps.model.model.request

import br.com.apps.model.dto.request.ItemDto
import br.com.apps.model.interfaces.ModelObjectInterface
import java.math.BigDecimal

data class Item(
    val masterUid: String,
    val id: String,
    val parentId: String,
    val value: BigDecimal,
    val description: String,
    val urlImage: String? = null,
    val isValid: Boolean
): ModelObjectInterface<ItemDto> {

    override fun toDto(): ItemDto = ItemDto(
        masterUid = masterUid,
        id = id,
        parentId = parentId,
        value = value.toDouble(),
        description = description,
        urlImage = urlImage,
        isValid = isValid
    )

}