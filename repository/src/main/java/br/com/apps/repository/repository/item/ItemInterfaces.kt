package br.com.apps.repository.repository.item

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.request.ItemDto
import br.com.apps.model.model.request.Item
import br.com.apps.repository.util.Response

interface ItemRepositoryInterface: ItemReadInterface, ItemWriteInterface

interface ItemReadInterface {

    /**
     * Fetches the [Item] dataSet for the specified ID.
     *
     * @param id The ID of the [Item].
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Item] list.
     */
    suspend fun fetchItemById(id: String, flow: Boolean = false): LiveData<Response<Item>>

    /**
     * Fetches the [Item] dataSet for the specified ID.
     *
     * @param id The ID of the parent.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Item] list.
     */
    suspend fun fetchItemsByParentId(id: String, flow: Boolean = false): LiveData<Response<List<Item>>>

    /**
     * Fetches the [Item] dataSet for the specified IDs.
     *
     * @param ids The ID of the parent's.
     * @param flow If the user wants to keep observing the data.
     * @return A [Response] with the [Item] list.
     */
    suspend fun fetchItemsByParentIds(ids: List<String>, flow: Boolean = false): LiveData<Response<List<Item>>>

}

interface ItemWriteInterface {

    /**
     * Saves the [ItemDto] object.
     * - If the ID of the item is null, it creates a new [Item].
     * - If the ID is not null, it updates the existing [Item].
     *
     * @param dto The [ItemDto] object to be saved.
     */
    suspend fun save(dto: ItemDto)

    /**
     * Deletes an [Item] document from the database based on the specified ID.
     *
     * @param id The ID of the item to be deleted.
     */
    suspend fun delete(id: String)

    suspend fun delete(ids: Array<String>)

}
