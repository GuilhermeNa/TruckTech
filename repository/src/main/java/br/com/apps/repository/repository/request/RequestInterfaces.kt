package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.request.RequestDto
import br.com.apps.model.model.User
import br.com.apps.model.model.request.Request
import br.com.apps.repository.util.Response

interface RequestRepositoryInterface : RequestWriteInterface, RequestReadInterface

interface RequestWriteInterface {

    /**
     * Updates the encoded image for a specific document in the Firestore collection.
     *
     * @param id The ID of the document to which the encoded image will be updated.
     * @param url The encoded image in String to be updated in the document.
     */
    suspend fun updateUrlImage(id: String, url: String?)

    suspend fun setUpdatingStatus(id: String, isUpdating: Boolean)

    /**
     * Saves the [TravelRequestDto] data in Firestore.
     *
     *  - If the ID of the Request Dto is null, it creates a new Request.
     *  - If the ID is not null, it updates the existing Request.
     *
     * @param dto The [TravelRequestDto] object to be saved.
     */
    suspend fun save(dto: RequestDto): String

    /**
     * This method is responsible for deleting the requests in Firestore.
     * @param id Is the ID of the request that must be deleted.
     */
    suspend fun delete(id: String)

}

interface RequestReadInterface {

    /**
     * Fetches the [Request] dataSet for the specified driver ID.
     *
     * @param id The ID of the [User].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Request] list.
     */
    suspend fun fetchRequestListByUid(id: String, flow: Boolean = false)
            : LiveData<Response<List<Request>>>

    /**
     * Fetches the [Request] dataSet for the specified ID.
     *
     * @param id The ID of the [Request].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Request] list.
     */
    suspend fun fetchRequestById(
        id: String,
        flow: Boolean = false
    ): LiveData<Response<Request>>

}