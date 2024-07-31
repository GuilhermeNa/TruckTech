package br.com.apps.repository.repository.request

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.dto.request.request.TravelRequestDto

class RequestRepository(
    private val read: RequestReadImpl,
    private val write: RequestWriteImpl
) : RequestRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: TravelRequestDto) = write.save(dto)

    override suspend fun saveItem(itemDto: RequestItemDto) = write.saveItem(itemDto)

    override suspend fun delete(requestId: String, itemIdList: List<String>?) =
        write.delete(requestId, itemIdList)

    override suspend fun deleteItem(requestId: String, itemId: String) =
        write.deleteItem(requestId, itemId)

    override suspend fun updateItemImageUrl(dto: RequestItemDto, url: String) =
        write.updateItemImageUrl(dto, url)

    override suspend fun updateRequestImageUrl(requestId: String, url: String) =
        write.updateRequestImageUrl(requestId, url)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchRequestListByDriverId(driverId: String, flow: Boolean) =
        read.fetchRequestListByDriverId(driverId, flow)

    override suspend fun fetchItemListByRequests(idList: List<String>, flow: Boolean) =
        read.fetchItemListByRequests(idList, flow)

    override suspend fun fetchRequestById(requestId: String, flow: Boolean) =
        read.fetchRequestById(requestId, flow)

    override suspend fun fetchItemListByRequestId(requestId: String, flow: Boolean) =
        read.fetchItemListByRequestId(requestId, flow)

    override suspend fun fetchItemById(requestId: String, itemId: String, flow: Boolean) =
        read.fetchItemById(requestId, itemId, flow)

}