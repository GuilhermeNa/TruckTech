package br.com.apps.repository.repository.request

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.repository.util.DOC_URL
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.ENCODED_IMAGE
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ITEMS
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REQUESTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class RequestWriteImpl(fireStore: FirebaseFirestore) : RequestWriteInterface {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    override suspend fun saveItem(itemDto: RequestItemDto): String {
        return if (itemDto.id == null) {
            createItem(itemDto)
        } else {
            updateItem(itemDto)
        }
    }

    private fun createItem(itemDto: RequestItemDto): String {
        val requestId = itemDto.requestId
            ?: throw NullPointerException("RequestRepository, createItem: requestId is null")

        val document =
            collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS).document()

        itemDto.id = document.id
        document.set(itemDto)
        return document.id
    }

    private fun updateItem(itemDto: RequestItemDto): String {
        val requestId = itemDto.requestId
            ?: throw NullPointerException("RequestRepository, updateItem: requestId is null")

        val itemId = itemDto.id
            ?: throw NullPointerException("RequestRepository, updateItem: id is null")

        collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS).document(itemId)
            .set(itemDto)
        return itemId
    }

    override suspend fun updateRequestImageUrl(requestId: String, url: String) {
        collection.document(requestId).update(ENCODED_IMAGE, url)
    }

    override suspend fun save(dto: TravelRequestDto): String {
        return if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private fun create(dto: TravelRequestDto): String {
        val document = collection.document()
        dto.id = document.id
        document.set(dto)
        return document.id
    }

    private fun update(dto: TravelRequestDto): String {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)
        collection.document(id).set(dto)
        return id
    }

    override suspend fun delete(requestId: String, itemIdList: List<String>?) {
        itemIdList?.forEach { itemId ->
            deleteItem(requestId, itemId)
        }
        collection.document(requestId).delete()
    }

    override suspend fun deleteItem(requestId: String, itemId: String) {
        collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS).document(itemId).delete()
    }

    override suspend fun updateItemImageUrl(dto: RequestItemDto, url: String) {
        val requestId = dto.requestId ?: throw InvalidParameterException(EMPTY_ID)
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection.document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS)
            .document(id)
            .update(mapOf(DOC_URL to url))
            .await()

    }

}