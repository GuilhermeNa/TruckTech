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

    override suspend fun getRequestListByDriverId(driverId: String, flow: Boolean) =
        read.getRequestListByDriverId(driverId, flow)

    override suspend fun getItemListByRequests(idList: List<String>, flow: Boolean) =
        read.getItemListByRequests(idList, flow)

    override suspend fun getRequestById(requestId: String, flow: Boolean) =
        read.getRequestById(requestId, flow)

    override suspend fun getItemListByRequestId(requestId: String, flow: Boolean) =
        read.getItemListByRequestId(requestId, flow)

    override suspend fun getItemById(requestId: String, itemId: String, flow: Boolean) =
        read.getItemById(requestId, itemId, flow)

}