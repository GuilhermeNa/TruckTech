package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.factory.RequestFactory
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.PaymentRequestStatusType
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.Resource
import br.com.apps.repository.Response
import br.com.apps.repository.repository.RequestRepository
import br.com.apps.trucktech.expressions.getKeyByValue
import br.com.apps.trucktech.ui.activities.main.DriverAndTruck
import kotlinx.coroutines.launch
import java.security.InvalidParameterException
import java.time.LocalDateTime

private const val ALL = "Todos"
private const val SENT = "Enviados"
private const val APPROVED = "Aprovados"
private const val DENIED = "Negados"
private const val PROCESSED = "Processados"

class RequestsListViewModel(
    private val driverAndTruck: DriverAndTruck,
    //private val useCase: RequestUseCase,
    private val repository: RequestRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [PaymentRequest]
     * to be displayed on screen.
     */
    private val _requestData = MutableLiveData<Response<List<PaymentRequest>>>()
    val requestData get() = _requestData

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

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        val id = driverAndTruck.user?.employeeId ?: throw InvalidParameterException(EMPTY_ID)

        viewModelScope.launch {
            val liveData =
                repository.getCompleteRequestListByDriverId(driverId = id, withFlow = false)
            liveData.asFlow().collect {
                _requestData.value = it
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
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val requestDto = driverAndTruck.let {
                    RequestFactory.createDto(
                        masterUid = it.user?.masterUid,
                        truckId = it.truck?.id,
                        driverId = it.user?.employeeId,
                        date = LocalDateTime.now(),
                        status = PaymentRequestStatusType.SENT.description
                    )
                }
                repository.save(requestDto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    /**
     * Delete an item
     */
    suspend fun delete(requestId: String, itemsIdList: List<String>?): LiveData<Resource<Boolean>> {
        return repository.delete(requestId, itemsIdList)
    }

    /**
     * Filter Data by header choise
     */
    fun filterDataByHeader(): List<PaymentRequest>? {
        val dataSet = (requestData.value as Response.Success<List<PaymentRequest>>).data
        return if (headerPos != null && dataSet != null) {
            when (headerPos) {
                0 -> dataSet.filter { it.status == PaymentRequestStatusType.SENT }
                1 -> dataSet.filter { it.status == PaymentRequestStatusType.APPROVED }
                2 -> dataSet.filter { it.status == PaymentRequestStatusType.DENIED }
                3 -> dataSet.filter { it.status == PaymentRequestStatusType.PROCESSED }
                4 -> dataSet
                else -> throw InvalidParameterException("Invalid header position")
            }
        } else {
            dataSet
        }
    }

}