package br.com.apps.repository.repository.request

import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.ENCODED_IMAGE
import br.com.apps.repository.util.FIRESTORE_COLLECTION_ITEMS
import br.com.apps.repository.util.FIRESTORE_COLLECTION_REQUESTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.security.InvalidParameterException

class RequestWrite(fireStore: FirebaseFirestore): RequestWriteI {

    private val collection = fireStore.collection(FIRESTORE_COLLECTION_REQUESTS)

    /**
     * Saves the [RequestItemDto] data in Firestore.
     *
     *  - If the ID of the Item is null, it creates a new Item.
     *  - If the ID is not null, it updates the existing Item.
     *
     * @param itemDto The [RequestItemDto] object to be saved.
     */
    override suspend fun saveItem(itemDto: RequestItemDto) {
        if (itemDto.id == null) {
            createItem(itemDto)
        } else {
            updateItem(itemDto)
        }
    }

    private suspend fun createItem(itemDto: RequestItemDto): String {
        val requestId = itemDto.requestId
            ?: throw NullPointerException("RequestRepository, createItem: requestId is null")

        val document =
            collection.document(requestId).collection(FIRESTORE_COLLECTION_ITEMS).document()

        itemDto.id = document.id

        document
            .set(itemDto)
            .await()

        return document.id
    }

    private suspend fun updateItem(itemDto: RequestItemDto) {
        val requestId = itemDto.requestId
            ?: throw NullPointerException("RequestRepository, updateItem: requestId is null")

        val itemId = itemDto.id
            ?: throw NullPointerException("RequestRepository, updateItem: id is null")

        collection.document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS)
            .document(itemId)
            .set(itemDto)
            .await()

    }

    /**
     * Updates the encoded image for a specific document in the Firestore collection.
     *
     * @param requestId The ID of the document to which the encoded image will be updated.
     * @param encodedImage The encoded image in String to be updated in the document.
     */
    override suspend fun updateEncodedImage(requestId: String, encodedImage: String) {
        collection
            .document(requestId)
            .update(ENCODED_IMAGE, encodedImage)
            .await()
    }

    /**
     * Saves the [TravelRequestDto] data in Firestore.
     *
     *  - If the ID of the Request Dto is null, it creates a new Request.
     *  - If the ID is not null, it updates the existing Request.
     *
     * @param dto The [TravelRequestDto] object to be saved.
     */
    override suspend fun save(dto: TravelRequestDto): String {
        return if (dto.id == null) {
            create(dto)
        } else {
            update(dto)
        }
    }

    private suspend fun create(dto: TravelRequestDto): String {
        val document = collection.document()
        dto.id = document.id

        document
            .set(dto)
            .await()

        return document.id
    }

    private suspend fun update(dto: TravelRequestDto): String {
        val id = dto.id ?: throw InvalidParameterException(EMPTY_ID)

        collection
            .document(id)
            .set(dto)
            .await()

        return id
    }

    /**
     * This method is responsible for deleting the requests in Firestore.
     * @param requestId Is the ID of the request that must be deleted.
     */
    override suspend fun delete(requestId: String, itemIdList: List<String>?) {
        itemIdList?.forEach { itemId ->
            deleteItem(requestId, itemId)
        }

        collection
            .document(requestId)
            .delete()
            .await()

    }

    /**
     * Deletes the item associated with the specified item ID from the request with the specified request ID in Firestore.
     *
     * @param requestId The ID of the request from which the item should be deleted.
     * @param itemId The ID of the item to be deleted.
     */
    override suspend fun deleteItem(requestId: String, itemId: String) {
        collection
            .document(requestId)
            .collection(FIRESTORE_COLLECTION_ITEMS)
            .document(itemId)
            .delete()
            .await()
    }

}