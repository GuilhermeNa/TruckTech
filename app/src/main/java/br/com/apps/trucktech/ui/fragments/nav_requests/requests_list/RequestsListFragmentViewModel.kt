package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.apps.model.factory.RequestFactory
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.PaymentRequestStatusType
import br.com.apps.repository.Resource
import br.com.apps.trucktech.expressions.getKeyByValue
import br.com.apps.trucktech.ui.activities.main.DriverAndTruck
import br.com.apps.usecase.RequestUseCase
import java.security.InvalidParameterException
import java.time.LocalDateTime

private const val ALL = "Todos"
private const val SENT = "Enviados"
private const val APPROVED = "Aprovados"
private const val DENIED = "Negados"
private const val PROCESSED = "Processados"

class RequestsListFragmentViewModel(
    private val driverAndTruck: DriverAndTruck,
    private val useCase: RequestUseCase
) : ViewModel() {

    /**
     * Load driver dataSet by driverId
     */
    val loadedRequestsData =
        driverAndTruck.user?.employeeId?.let { useCase.getAllRequestsAndItemsByDriverId(it) }

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

    fun newHeaderSelected(headerTitle: String) {
        _headerPos = headerItemsMap.getKeyByValue(headerTitle)
    }

    suspend fun createNewRequest(): LiveData<Resource<String>> {
        val requestDto = driverAndTruck.let {
            RequestFactory.createDto(
                masterUid = it.user?.masterUid,
                truckId = it.truck?.id,
                driverId = it.user?.employeeId,
                date = LocalDateTime.now(),
                status = PaymentRequestStatusType.SENT.description
            )
        }
        return useCase.saveRequest(requestDto)
    }

    /**
     * Delete an item
     */
    suspend fun delete(requestId: String, itemsIdList: List<String>?): LiveData<Resource<Boolean>> {
        return useCase.deleteRequest(requestId, itemsIdList)
    }

    /**
     * Filter Data by header choise
     */
    fun filterDataByHeader(): List<PaymentRequest>? {
        val dataSet = loadedRequestsData?.value?.data

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