package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.RequestDto
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.enums.PaymentRequestStatusType
import br.com.apps.model.exceptions.null_objects.NullItemException
import br.com.apps.model.exceptions.null_objects.NullRequestException
import br.com.apps.model.expressions.atBrZone
import br.com.apps.model.model.request.Item
import br.com.apps.model.model.request.Request
import br.com.apps.model.model.request.Request.Companion.merge
import br.com.apps.model.util.toDate
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.trucktech.expressions.getKeyByValue
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.RequestUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.InvalidParameterException
import java.time.LocalDateTime

private const val ALL = "Todos"
private const val SENT = "Enviados"
private const val APPROVED = "Aprovados"
private const val DENIED = "Negados"
private const val PROCESSED = "Processados"

class RequestsListViewModel(
    private val vmData: RequestLVMData,
    private val requestRepo: RequestRepository,
    private val itemRepo: ItemRepository,
    private val useCase: RequestUseCase,
) : ViewModel() {

    private var fetchItemsJob: Job? = null

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [PaymentRequest]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Request>>()
    val data get() = _data

    private val _state = MutableLiveData<State>()
    val state get() = _state

    /**
     * Header Position
     */
    private var _headerPos: Int? = 0
    val headerPos get() = _headerPos

    /**
     * data for header recycler
     */
    val headerItemsMap = mapOf(
        Pair(0, SENT),
        Pair(1, APPROVED),
        Pair(2, DENIED),
        Pair(3, PROCESSED),
        Pair(4, ALL)
    )

    private val _dialog = MutableLiveData<Boolean>()
    val dialog get() = _dialog

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
        loadData()
    }

    fun loadData() {

        suspend fun fetchRequests(complete: (requests: List<Request>) -> Unit) {
            requestRepo.fetchRequestListByUid(vmData.uid, true)
                .asFlow().collect { response ->
                    when (response) {
                        is Response.Error -> throw response.exception
                        is Response.Success -> response.data?.let { complete(it) }
                            ?: throw NullRequestException()
                    }
                }
        }

        fun CoroutineScope.fetchItems(
            requests: List<Request>, complete: (items: List<Item>) -> Unit
        ) {
            fetchItemsJob?.cancel()

            fetchItemsJob = launch {
                val ids = requests.map { it.id }
                itemRepo.fetchItemsByParentIds(ids, true)
                    .asFlow().collect { response ->
                        when (response) {
                            is Response.Error -> emptyList()
                            is Response.Success -> response.data ?: throw NullItemException()
                        }.run { complete(this) }
                    }
            }
        }

        fun sendResponse(requests: List<Request>) {
            if (requests.isEmpty()) {
                setState(State.Empty)

            } else {
                setState(State.Loaded)
                _data.value = requests
            }
        }

        viewModelScope.launch {
            delay(1000)

            try {

                fetchRequests { requests ->
                    fetchItems(requests) { items ->
                        requests.merge(items)
                        sendResponse(requests)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))
            }
        }

    }

    fun newHeaderSelected(headerTitle: String) {
        _headerPos = headerItemsMap.getKeyByValue(headerTitle)
    }

    /**
     * Send the [PaymentRequest] to be created or updated.
     */
    suspend fun save() =
        liveData(viewModelScope.coroutineContext) {
            try {
                val dto = createDto()
                val id = useCase.createRequest(dto, vmData.uid)
                emit(Response.Success(id))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

    private fun createDto() =
        RequestDto(
            masterUid = vmData.masterUid,
            uid = vmData.uid,
            date = LocalDateTime.now().atBrZone().toDate(),
            status = PaymentRequestStatusType.SENT.name,
            requestNumber = 1
        )

    /**
     * Delete an item
     */
    suspend fun delete(id: String) =
        liveData<Response<Unit>>((viewModelScope.coroutineContext)) {
            try {
                useCase.delete(
                    writeReq = WriteRequest(
                        vmData.permission,
                        (_data.value)!!.first { it.id == id }.toDto()
                    ),
                    items = (_data.value)!!
                        .first { it.id == id }.items
                        .map { it.toDto() }
                        .toTypedArray()
                )
                emit(Response.Success())

            } catch (e: Exception) {
                emit(Response.Error(e))

            }
        }

    /**
     * Filter Data by header choise
     */
    fun filterDataByHeader(): List<Request>? {
        val dataSet = data.value
        return if (headerPos != null && dataSet != null) {
            when (headerPos) {
                0 -> {
                    val data = dataSet.filter { it.status == PaymentRequestStatusType.SENT }
                    if (data.isEmpty()) _state.value = State.Empty
                    else _state.value = State.Loaded
                    data
                }

                1 -> {
                    val data = dataSet.filter { it.status == PaymentRequestStatusType.APPROVED }
                    if (data.isEmpty()) _state.value = State.Empty
                    else _state.value = State.Loaded
                    data
                }

                2 -> {
                    val data = dataSet.filter { it.status == PaymentRequestStatusType.DENIED }
                    if (data.isEmpty()) _state.value = State.Empty
                    else _state.value = State.Loaded
                    data
                }

                3 -> {
                    val data =
                        dataSet.filter { it.status == PaymentRequestStatusType.PROCESSED }
                    if (data.isEmpty()) _state.value = State.Empty
                    else _state.value = State.Loaded
                    data
                }

                4 -> {
                    if (dataSet.isEmpty()) _state.value = State.Empty
                    else _state.value = State.Loaded
                    dataSet
                }

                else -> throw InvalidParameterException("RequestListViewModel, filterDataByHeader: Invalid header position $headerPos")
            }
        } else {
            _state.value = State.Error(NullPointerException(EMPTY_DATASET))
            dataSet
        }
    }

    fun dialogRequested() {
        _dialog.value = true
    }

    fun dialogDismissed() {
        _dialog.value = false
    }

    private fun setState(state: State) {
        _state.value = state
    }

}

data class RequestLVMData(
    val masterUid: String,
    val uid: String,
    val truckId: String,
    val driverId: String,
    val permission: AccessLevel
)
