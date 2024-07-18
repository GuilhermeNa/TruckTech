package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.PaymentRequestStatusType
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.model.toDate
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.Response
import br.com.apps.model.expressions.atBrZone
import br.com.apps.trucktech.expressions.getKeyByValue
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.RequestUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
    private val repository: RequestRepository,
    private val useCase: RequestUseCase,
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [PaymentRequest]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<PaymentRequest>>()
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
    }

    fun loadData() {
        viewModelScope.launch {
            delay(1000)

            try {

                val requests = loadRequestsAwait()
                val idList = requests.mapNotNull { it.id }

                val items = loadItemsAwait(idList)
                useCase.mergeRequestData(requests, items)

                if (requests.isEmpty()) setState(State.Empty)
                else setState(State.Loaded)

                _data.value = requests

            } catch (e: Exception) {
                _state.value = State.Error(e)

            }
        }
    }

    private suspend fun loadRequestsAwait(): List<PaymentRequest> {
        val deferred = CompletableDeferred<List<PaymentRequest>>()

        repository.getRequestListByDriverId(driverId = vmData.driverId)
            .asFlow().first { response ->
                when (response) {
                    is Response.Error -> throw response.exception

                    is Response.Success -> response.data?.let { deferred.complete(it) }
                        ?: deferred.complete(emptyList())
                }
                true
            }

        return deferred.await()
    }

    private suspend fun loadItemsAwait(idList: List<String>): List<RequestItem> {
        val deferred = CompletableDeferred<List<RequestItem>>()

        repository.getItemListByRequests(idList, true)
            .asFlow().first { response ->
                when (response) {
                    is Response.Error -> throw response.exception

                    is Response.Success -> response.data?.let { deferred.complete(it) }
                        ?: deferred.complete(emptyList())
                }
                true
            }

        return deferred.await()
    }

    fun newHeaderSelected(headerTitle: String) {
        _headerPos = headerItemsMap.getKeyByValue(headerTitle)
    }

    /**
     * Send the [PaymentRequest] to be created or updated.
     */
    suspend fun save() =
        liveData(viewModelScope.coroutineContext) { //TODO testando este metodo
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
        TravelRequestDto(
            masterUid = vmData.masterUid,
            truckId = vmData.truckId,
            driverId = vmData.driverId,
            date = LocalDateTime.now().atBrZone().toDate(),
            status = PaymentRequestStatusType.SENT.description,
        )

    /**
     * Delete an item
     */
    suspend fun delete(request: PaymentRequest) =
        liveData<Response<Unit>>((viewModelScope.coroutineContext)) {
            try {
                useCase.delete(vmData.permission, request)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    /**
     * Filter Data by header choise
     */
    fun filterDataByHeader(): List<PaymentRequest>? {
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
                    val data = dataSet.filter { it.status == PaymentRequestStatusType.PROCESSED }
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
    val permission: PermissionLevelType
)
