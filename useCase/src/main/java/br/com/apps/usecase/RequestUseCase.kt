package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.repository.Resource
import br.com.apps.repository.repository.RequestRepository

/**
 * Use case class responsible for handling business logic related to payment requests and items.
 *
 * @param repository The repository instance to interact with Firestore.
 */
class RequestUseCase(private val repository: RequestRepository) {

    /**
     * Saves a payment request to Firestore.
     *
     * @param requestDto The PaymentRequestDto object containing the data to be saved.
     * @return A LiveData<Resource<String>> representing the ID of the saved document.
     */
    suspend fun saveRequest(requestDto: PaymentRequestDto): LiveData<Resource<String>> {
        val liveData = MutableLiveData<Resource<String>>()
        val id = repository.saveRequest(requestDto)
        liveData.value = Resource(data = id)
        return liveData
    }

    /**
     * Retrieves all payment requests and their associated items for a given driver ID from Firestore.
     *
     * @param driverId The ID of the driver for whom the requests and items should be retrieved.
     * @return A LiveData<Resource<List<PaymentRequest>>> representing the result of the retrieval operation.
     */
    fun getAllRequestsAndItemsByDriverId(driverId: String): LiveData<Resource<List<PaymentRequest>>> {
        return repository.getAllRequestsAndItemsByDriverId(driverId)
    }

    /**
     * Retrieves the payment request and its associated items for the specified request ID from Firestore.
     *
     * @param requestId The ID of the request for which the request and items should be retrieved.
     * @return A LiveData<Resource<PaymentRequest>> representing the result of the retrieval operation.
     */
    fun getRequestAndItemsById(requestId: String): LiveData<Resource<PaymentRequest>> {
        return repository.getRequestAndItemsById(requestId)
    }

    /**
     * Saves a request item to Firestore.
     *
     * @param requestId The ID of the request to which the item belongs.
     * @param requestItem The RequestItemDto object containing the data to be saved.
     * @return A LiveData<Resource<Boolean>> representing the result of the save operation.
     */
    suspend fun saveItem(requestId: String, requestItem: RequestItemDto): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        repository.saveItem(requestId, requestItem)
        liveData.value = Resource(data = true)
        return liveData
    }

    /**
     * Deletes a request from Firestore along with its associated items.
     *
     * @param requestId The ID of the request to be deleted.
     * @param itemsIdList The list of IDs of items associated with the request to be deleted.
     * @return A LiveData<Resource<Boolean>> representing the result of the deletion operation.
     */
    fun deleteRequest(requestId: String, itemsIdList: List<String>?): LiveData<Resource<Boolean>> {
        itemsIdList?.forEach { itemId ->
            deleteItem(requestId, itemId)
        }
        return repository.deleteRequest(requestId)
    }

    /**
     * Deletes an item associated with the specified item ID from the request with the specified request ID in Firestore.
     *
     * @param requestId The ID of the request from which the item should be deleted.
     * @param itemId The ID of the item to be deleted.
     * @return A LiveData<Resource<Boolean>> representing the result of the deletion operation.
     */
    fun deleteItem(requestId: String, itemId: String): LiveData<Resource<Boolean>> {
        return repository.deleteItem(requestId, itemId)
    }

}