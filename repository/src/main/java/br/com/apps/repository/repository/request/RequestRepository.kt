package br.com.apps.repository.repository.request

import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.dto.request.request.RequestItemDto

/**
 * Repository class responsible for interacting with Firestore to manage payment requests and items.
 * @param fireStore The instance of FirebaseFirestore to use for database operations.
 */
class RequestRepository(
    private val read: RequestRead,
    private val write: RequestWrite
): RequestRepositoryI {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: PaymentRequestDto) = write.save(dto)

    override suspend fun saveItem(itemDto: RequestItemDto) = write.saveItem(itemDto)

    override suspend fun delete(requestId: String, itemIdList: List<String>?) = write.delete(requestId, itemIdList)

    override suspend fun deleteItem(requestId: String, itemId: String) = write.deleteItem(requestId, itemId)

    override suspend fun updateEncodedImage(requestId: String, encodedImage: String) =
        write.updateEncodedImage(requestId, encodedImage)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getCompleteRequestListByDriverId(driverId: String, flow: Boolean) =
        read.getCompleteRequestListByDriverId(driverId, flow)

    override suspend fun getCompleteRequestById(requestId: String, flow: Boolean) =
        read.getCompleteRequestById(requestId, flow)

    override suspend fun getItemById(requestId: String, itemId: String, flow: Boolean) =
        read.getItemById(requestId, itemId, flow)

}