package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.repository.util.Response

interface RequestRepositoryInterface : RequestWriteInterface, RequestReadInterface

interface RequestWriteInterface {

    /**
     * Saves the [RequestItemDto] data in Firestore.
     *
     *  - If the ID of the Item is null, it creates a new Item.
     *  - If the ID is not null, it updates the existing Item.
     *
     * @param itemDto The [RequestItemDto] object to be saved.
     */
    suspend fun saveItem(itemDto: RequestItemDto): String

    /**
     * Updates the encoded image for a specific document in the Firestore collection.
     *
     * @param requestId The ID of the document to which the encoded image will be updated.
     * @param url The encoded image in String to be updated in the document.
     */
    suspend fun updateRequestImageUrl(requestId: String, url: String)

    /**
     * Saves the [TravelRequestDto] data in Firestore.
     *
     *  - If the ID of the Request Dto is null, it creates a new Request.
     *  - If the ID is not null, it updates the existing Request.
     *
     * @param dto The [TravelRequestDto] object to be saved.
     */
    suspend fun save(dto: TravelRequestDto): String

    /**
     * This method is responsible for deleting the requests in Firestore.
     * @param requestId Is the ID of the request that must be deleted.
     */
    suspend fun delete(requestId: String, itemIdList: List<String>? = null)

    /**
     * Deletes the item associated with the specified item ID from the request with the specified request ID in Firestore.
     *
     * @param requestId The ID of the request from which the item should be deleted.
     * @param itemId The ID of the item to be deleted.
     */
    suspend fun deleteItem(requestId: String, itemId: String)

    /**
     * Updates the encoded image for a specific item document in the Firestore collection.
     *
     * @param dto The [RequestItemDto] whose URL needs to be updated.
     * @param url The encoded image in String to be updated in the document.
     */
    suspend fun updateItemImageUrl(dto: RequestItemDto, url: String)

}

interface RequestReadInterface {

    /**
     * Fetches the [PaymentRequest] dataSet for the specified driver ID.
     *
     * @param driverId The ID of the [Employee].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    suspend fun fetchRequestListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<PaymentRequest>>>

    /**
     * Fetches the [RequestItem] dataSet for the specified request's ID.
     *
     * @param idList The ID's of the [PaymentRequest]'s.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [RequestItem] list.
     */
    suspend fun fetchItemListByRequests(idList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<RequestItem>>>

    /**
     * Fetches the [PaymentRequest] dataSet for the specified ID.
     *
     * @param requestId The ID of the [PaymentRequest].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    suspend fun fetchRequestById(
        requestId: String,
        flow: Boolean = false
    ): LiveData<Response<PaymentRequest>>

    /**
     * Fetches the [RequestItem] dataSet for the specified request ID.
     *
     * @param requestId The ID of the [PaymentRequest].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [RequestItem] list.
     */
    suspend fun fetchItemListByRequestId(requestId: String, flow: Boolean = false)
            : LiveData<Response<List<RequestItem>>>

    /**
     * Fetches the [PaymentRequest] dataSet for the specified ID.
     *
     * @param requestId The ID of the [PaymentRequest].
     * @param itemId The ID of the [RequestItem].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [PaymentRequest] list.
     */
    suspend fun fetchItemById(
        requestId: String,
        itemId: String,
        flow: Boolean = false
    ): LiveData<Response<RequestItem>>

}