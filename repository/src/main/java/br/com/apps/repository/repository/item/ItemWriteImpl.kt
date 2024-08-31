package br.com.apps.repository.repository.item

import br.com.apps.model.dto.request.ItemDto
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ITEMS
import br.com.apps.repository.util.IS_UPDATING
import br.com.apps.repository.util.URL_IMAGE
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class ItemWriteImpl(firestore: FirebaseFirestore) : ItemWriteInterface {

    private val collection = firestore.collection(FIRESTORE_COLLECTION_ITEMS)

    override suspend fun save(dto: ItemDto) = withContext(Dispatchers.IO) {
        return@withContext when (dto.id) {
            null -> create(dto)
            else -> update(dto)
        }
    }

    private fun create(dto: ItemDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

    private fun update(dto: ItemDto): String {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
        return id
    }

    override suspend fun delete(id: String) {
        withContext(Dispatchers.IO) {
            when {
                id.isEmpty() -> throw EmptyDataException("Id is null")
                else -> collection.document(id).delete()
            }
        }
    }

    override suspend fun delete(ids: Array<String>) {
        withContext(Dispatchers.IO) {
            when {
                ids.isEmpty() -> throw EmptyDataException("Id is null")
                else -> ids.forEach { collection.document(it).delete() }
            }
        }
    }

    override suspend fun updateUrl(id: String, url: String?) {
        withContext(Dispatchers.IO) {
            if (id.isEmpty()) throw NullPointerException()

            val updates = mutableMapOf<String, Any?>(
                Pair(URL_IMAGE, url),
                Pair(IS_UPDATING, false)
            )

            collection.document(id).update(updates)
        }
    }

}