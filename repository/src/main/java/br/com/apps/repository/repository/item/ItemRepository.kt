package br.com.apps.repository.repository.item

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.request.ItemDto
import br.com.apps.model.model.request.Item
import br.com.apps.repository.util.Response

class ItemRepository(private val read: ItemReadImpl, private val write: ItemWriteImpl) :
    ItemRepositoryInterface {

    override suspend fun fetchItemById(
        id: String, flow: Boolean
    ): LiveData<Response<Item>> = read.fetchItemById(id, flow)

    override suspend fun fetchItemsByParentId(
        id: String,
        flow: Boolean
    ): LiveData<Response<List<Item>>> = read.fetchItemsByParentId(id, flow)

    override suspend fun fetchItemsByParentIds(
        ids: List<String>,
        flow: Boolean
    ): LiveData<Response<List<Item>>> = read.fetchItemsByParentIds(ids, flow)

    override suspend fun save(dto: ItemDto) = write.save(dto)

    override suspend fun delete(id: String) = write.delete(id)

    override suspend fun delete(ids: Array<String>) = write.delete(ids)

}