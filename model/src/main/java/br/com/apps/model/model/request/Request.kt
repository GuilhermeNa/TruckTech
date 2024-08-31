package br.com.apps.model.model.request

import br.com.apps.model.dto.request.RequestDto
import br.com.apps.model.enums.PaymentRequestStatusType
import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.exceptions.null_objects.NullItemException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.DUPLICATED_ID
import br.com.apps.model.util.toDate
import java.time.LocalDateTime

data class Request(
    val masterUid: String,
    val id: String,
    val uid: String,
    val requestNumber: Long,
    val date: LocalDateTime,
    val urlImage: String? = null,
    val isUpdating: Boolean,
    val status: PaymentRequestStatusType
) : ModelObjectInterface<RequestDto> {

    private val _items = mutableListOf<Item>()
    val items get() = _items

    companion object {
        fun List<Request>.merge(items: List<Item>) {
            this.forEach { request ->
                items.filter { it.parentId == request.id }
                    .let { request.addAll(it) }
            }
        }
    }

    fun getValue() = _items.sumOf { it.value }

    fun addItem(item: Item) {
        val existingIds = _items.asSequence().map { it.id }.toSet()
        if (item.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        else if (item.parentId != id) throw InvalidIdException("The parent id for item is different.")
        _items.add(item)
    }

    fun addAll(items: List<Item>) {
        _items.clear()
        val distinctItems = items.distinctBy { it.id }.filter { it.parentId == id }
        _items.addAll(distinctItems)
    }

    override fun toDto(): RequestDto = RequestDto(
        masterUid = masterUid,
        id = id,
        uid = uid,
        urlImage = urlImage,
        requestNumber = requestNumber,
        date = date.toDate(),
        status = status.name,
        isUpdating = isUpdating
    )

    fun getItemById(itemId: String): Item = _items.firstOrNull() { it.id == itemId} ?: throw NullItemException()


}
