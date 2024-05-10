package br.com.apps.model.factory

import br.com.apps.model.factory.FactoryUtil.Companion.checkIfStringsAreBlank
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.model.model.request.request.RequestItemType
import java.math.BigDecimal
import java.security.InvalidParameterException

object RequestItemFactory {

    const val TAG_REQUEST_ID = "requestId"
    const val TAG_LABEL_ID = "labelId"
    const val TAG_KM_MARKING = "kmMarking"
    const val TAG_VALUE = "value"
    private const val TAG_TYPE = "type"

    /**
     * Creates a new [RequestItem] object based on the provided mapped fields and item type.
     *
     * @param mappedFields A HashMap containing key-value pairs representing the fields of the request item.
     * @param type The type of the request item to be created.
     * @return The created RequestItem object.
     * @throws NullPointerException if the required fields (requestId or value) are null.
     * @throws IllegalArgumentException if the requestId or value fields are blank.
     */
    fun create(mappedFields: HashMap<String, String>, type: RequestItemType): RequestItem {
        val requestId = mappedFields[TAG_REQUEST_ID]
            ?: throw NullPointerException("RequestItemFactory, create: requestId is null")

        val value = mappedFields[TAG_VALUE]
            ?: throw NullPointerException("RequestItemFactory, create: value is null")

        checkIfStringsAreBlank(requestId, value)

        val item = RequestItem(
            requestId = requestId,
            value = BigDecimal(value),
            type = type
        )

        return when (type) {
            RequestItemType.REFUEL -> appendRefuelItemData(item, mappedFields)
            RequestItemType.COST -> appendCostItemData(item, mappedFields)
            RequestItemType.WALLET -> item
        }
    }

    private fun appendCostItemData(item: RequestItem, mappedFields: HashMap<String, String>): RequestItem {
        val labelId = mappedFields[TAG_LABEL_ID]
            ?: throw NullPointerException("RequestItemFactory, appendCostItemData: labelId is null")

        checkIfStringsAreBlank(labelId)
        item.labelId = labelId
        return item
    }

    private fun appendRefuelItemData(item: RequestItem, mappedFields: HashMap<String, String>): RequestItem {
        val kmMarking = mappedFields[TAG_KM_MARKING]
            ?: throw NullPointerException("RequestItemFactory, appendRefuelItemData: kmMarking is null")

        checkIfStringsAreBlank(kmMarking)
        item.kmMarking = kmMarking.toInt()
        return item
    }

    /**
     * Updates the fields of a [RequestItem] object based on the provided key-value pairs.
     *
     * @param item The RequestItem object to be updated.
     * @param mapFields A HashMap containing key-value pairs representing the fields to be updated.
     * @throws IllegalArgumentException if any of the provided values are blank or if an invalid field key is provided.
     */
    fun update(item: RequestItem, mapFields: HashMap<String, String>) {
        mapFields.forEach { (key, value) ->

            checkIfStringsAreBlank(value)

            when (key) {
                TAG_KM_MARKING -> item.kmMarking = value.toInt()
                TAG_VALUE -> item.value = BigDecimal(value)
                TAG_TYPE -> item.type = RequestItemType.getType(value)
                TAG_LABEL_ID -> item.labelId = value
                else -> throw InvalidParameterException("RequestItemFactory, update: Impossible update this field ($key)")
            }

        }
    }

}