package br.com.apps.model.factory

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.factory.FactoryUtil.Companion.checkIfStringsAreBlank
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.model.model.request.request.RequestItemType
import java.math.BigDecimal
import java.security.InvalidParameterException

object RequestItemFactory {

    /**
     * Creates a new [RequestItem] object based on the provided mapped fields and item type.
     *
     * @param viewDto The data from the view to be created.
     * @param type The type of the request item to be created.
     * @return The created RequestItem object.
     * @throws NullPointerException if the required fields (requestId or value) are null.
     * @throws IllegalArgumentException if the requestId or value fields are blank.
     */
    fun create(viewDto: RequestItemDto, type: RequestItemType): RequestItem {
        val item = RequestItem(
            requestId = viewDto.requestId!!,
            value = BigDecimal(viewDto.value!!),
            type = type
        )

        if (!viewDto.validateFields())
            throw InvalidParameterException("RequestItemFactory, create: ($viewDto)")

        throw return when (type) {
            RequestItemType.REFUEL -> appendRefuelItemData(item, viewDto)
            RequestItemType.COST -> appendCostItemData(item, viewDto)
            RequestItemType.WALLET -> item
        }
    }

    private fun appendCostItemData(
        item: RequestItem,
        viewDto: RequestItemDto
    ): RequestItem {
        if(viewDto.labelId == null)
            throw NullPointerException("RequestItemFactory, appendCostItemData: labelId is null")

        checkIfStringsAreBlank(viewDto.labelId)
        item.labelId = viewDto.labelId
        return item
    }

    private fun appendRefuelItemData(
        item: RequestItem,
        viewDto: RequestItemDto
    ): RequestItem {
        if(viewDto.kmMarking == null)
            throw NullPointerException("RequestItemFactory, appendRefuelItemData: kmMarking is null")

        item.kmMarking = viewDto.kmMarking
        return item
    }

    /**
     * Updates the fields of a [RequestItem] object based on the provided key-value pairs.
     *
     * @param item The RequestItem object to be updated.
     * @param viewDto The data from the view to be updated.
     * @throws IllegalArgumentException if any of the provided values are blank or if an invalid field key is provided.
     */
    fun update(item: RequestItem, viewDto: RequestItemDto) {
        viewDto.labelId?.let { item.labelId = it }
        viewDto.kmMarking?.let { item.kmMarking = it }
        viewDto.value?.let { item.value = BigDecimal(it) }
        viewDto.type?.let { item.type = RequestItemType.getType(it) }
    }

}