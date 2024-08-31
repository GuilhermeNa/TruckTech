package br.com.apps.trucktech.service

import androidx.lifecycle.asFlow
import br.com.apps.model.exceptions.null_objects.NullItemException
import br.com.apps.model.exceptions.null_objects.NullRequestException
import br.com.apps.model.model.request.Item
import br.com.apps.model.model.request.Request
import br.com.apps.model.model.request.Request.Companion.merge
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RequestService(
    private val requestRepository: RequestRepository,
    private val itemRepository: ItemRepository,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun close() = scope.cancel()

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    fun fetchRequestsByUser(uid: String, complete: (List<Request>) -> Unit) {
        var itemsJob: Job? = null

        fetchRequests(uid) { requests ->
            itemsJob?.cancel()
            itemsJob = fetchItems(requests) { items ->
                requests.merge(items)
                complete(requests)
            }
        }

    }

    private fun fetchRequests(
        uid: String,
        complete: (requests: List<Request>) -> Unit
    ) = scope.launch {
        requestRepository.fetchRequestListByUid(uid, true).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data ?: throw NullRequestException()
            }.run { complete(this) }
        }
    }

    private fun fetchItems(
        requests: List<Request>,
        complete: (items: List<Item>) -> Unit
    ) = scope.launch {
        val ids = requests.map { it.id }
        itemRepository.fetchItemsByParentIds(ids, true).asFlow().collect { response ->
            when (response) {
                is Response.Error -> emptyList()
                is Response.Success -> response.data ?: throw NullItemException()
            }.run { complete(this) }
        }
    }

}