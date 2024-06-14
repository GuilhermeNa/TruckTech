package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.factory.FreightFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.Customer
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.model.toDate
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.FreightUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class FreightEditorViewModel(
    private val vmData: FreightEVMData,
    private val repository: FreightRepository,
    private val customerRepository: CustomerRepository,
    private val useCase: FreightUseCase,
) : ViewModel() {

    private var isEditing: Boolean = vmData.freightId?.let { true } ?: false

    /**
     * LiveData containing the [LocalDateTime] of the [Freight] date,
     * or LocalDateTime.now() if there is no [Freight] to be loaded.
     */
    private val _date = MutableLiveData<LocalDateTime>()
    val date get() = _date

    /**
     * if there is an [Freight] to be loaded, this liveData is
     * responsible for holding the [Response] data of the [Freight].
     */
    private val _data = MutableLiveData<FreightEFData>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {

        loadData { customers, freight ->
            processData(customers, freight)
            sendResponse(customers, freight)
        }

    }

    /**
     * Loads data asynchronously and set [_data] response.
     *
     * @param [freightId] is the id of the freight to be loaded
     */
    private fun loadData(complete: (customers: List<Customer>, freight: Freight?) -> Unit) {
        viewModelScope.launch {

            val customerListDef = loadCustomers()
            val freightDef = vmData.freightId?.let { loadFreight(it) }

            val customers = customerListDef.await()
            val freight = freightDef?.await()

            complete(customers, freight)

        }
    }

    private suspend fun loadFreight(freightId: String): CompletableDeferred<Freight> {
        val deferred = CompletableDeferred<Freight>()

        repository.getFreightById(freightId).asFlow().first { response ->
            when (response) {
                is Response.Error -> deferred.completeExceptionally(response.exception)
                is Response.Success -> {
                    response.data?.let { deferred.complete(it) }
                        ?: deferred.completeExceptionally(NullPointerException())
                }
            }
            true
        }

        return deferred
    }

    private suspend fun loadCustomers(): CompletableDeferred<List<Customer>> {
        val deferred = CompletableDeferred<List<Customer>>()

        customerRepository.getCustomerListByMasterUid(vmData.masterUid).asFlow().first { response ->
            when (response) {
                is Response.Error -> deferred.completeExceptionally(response.exception)
                is Response.Success ->
                    response.data?.let { deferred.complete(it) }
                        ?: deferred.completeExceptionally(NullPointerException())

            }
            true
        }

        return deferred
    }

    private fun processData(customers: List<Customer>, freight: Freight?) {
        freight?.apply {
            customer = customers.first { it.id == customerId }
        }

        _date.value =
            freight?.let {
                it.loadingDate ?: LocalDateTime.now()
            } ?: LocalDateTime.now()
    }

    private fun sendResponse(customers: List<Customer>, freight: Freight?) {
        _data.value =
            if (freight == null) {
                FreightEFData(customerList = customers)
            } else {
                FreightEFData(customerList = customers, freight = freight)
            }

    }

    /**
     * Interact with the [_date] LiveData, changing it.
     */
    fun newDateHaveBeenSelected(dateInLong: Long) {
        val datetime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(dateInLong), ZoneId.systemDefault())
        _date.value = datetime
    }

    /**
     * Send the [Freight] to be created or updated.
     */
    fun save(viewDto: FreightDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = createOrUpdate(viewDto)
                useCase.save(vmData.permissionLevel, dto)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

    private fun createOrUpdate(viewDto: FreightDto): FreightDto {
        viewDto.apply {
            loadingDate = date.value!!.toDate()
            isCommissionPaid = false
            customerId = data.value!!.customerList.first { it.name == customer }.id
            customer = null

        }

        return when (isEditing) {
            true -> {
                val freight = data.value!!.freight!!
                FreightFactory.update(freight, viewDto)
                freight.toDto()
            }
            false -> {
                viewDto.apply {
                    masterUid = vmData.masterUid
                    truckId = vmData.truckId
                    travelId = vmData.travelId
                    driverId = vmData.driverId
                    commissionPercentual = vmData.commissionPercentual.toDouble()
                    isValid = false
                }
                FreightFactory.create(viewDto).toDto()
            }
        }

    }

    fun validateCustomer(customer: String): Boolean {
        var isValid = true

        if (customer.isEmpty()) isValid = false

        val customers = data.value!!.customerList.map { it.name }
        if (!customers.contains(customer)) isValid = false

        return isValid
    }

}

data class FreightEFData(
    val customerList: List<Customer>,
    val freight: Freight? = null
)

data class FreightEVMData(
    val masterUid: String,
    val truckId: String,
    val travelId: String,
    val driverId: String,
    val freightId: String? = null,
    val commissionPercentual: BigDecimal,
    val permissionLevel: PermissionLevelType
)