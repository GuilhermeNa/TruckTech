package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.exceptions.null_objects.NullCustomerException
import br.com.apps.model.exceptions.null_objects.NullFreightException
import br.com.apps.model.expressions.atBrZone
import br.com.apps.model.model.Customer
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.user.AccessLevel
import br.com.apps.model.util.toDate
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.repository.util.WriteRequest
import br.com.apps.usecase.usecase.FreightUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class FreightEditorViewModel(
    private val vmData: FreightEVMData,
    private val repository: FreightRepository,
    private val customerRepository: CustomerRepository,
    private val useCase: FreightUseCase,
) : ViewModel() {

    private var isEditing: Boolean = vmData.freightId?.let { true } ?: false

    private lateinit var customers: List<Customer>

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
        loadData()
    }

    private fun setDate(date: LocalDateTime) {
        _date.value = date
    }

    private fun setFragmentData(fragData: FreightEFData) {
        _data.value = fragData
    }

    /**
     * Loads data asynchronously and set [_data] response.
     *
     * @param [freightId] is the id of the freight to be loaded
     */
    private fun loadData() {
        viewModelScope.launch {
            try {
                customers = loadCustomers()

                val nFreight = vmData.freightId?.let { loadFreight(it) }

                sendResponse(nFreight)

            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private suspend fun loadCustomers(): List<Customer> {
        val response =
            customerRepository.fetchCustomerListByMasterUid(vmData.masterUid).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullCustomerException(UNKNOWN_EXCEPTION)
        }
    }

    private suspend fun loadFreight(freightId: String): Freight {
        val response = repository.fetchFreightById(freightId).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data
                ?: throw NullFreightException(UNKNOWN_EXCEPTION)
        }
    }

    private fun sendResponse(freight: Freight?) {
        if (freight == null) {
            setDate(LocalDateTime.now().atBrZone())
            FreightEFData(customerList = customers)
        } else {
            freight.setCustomerById(customers)
            setDate(freight.loadingDate)
            setFragmentData(FreightEFData(customerList = customers, freight = freight))
        }
    }

    /**
     * Interact with the [_date] LiveData, changing it.
     */
    fun newDateHaveBeenSelected(dateInLong: Long) {
        val datetime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dateInLong), ZoneOffset.UTC
        )
        setDate(datetime)
    }

    /**
     * Send the [Freight] to be created or updated.
     */
    fun save(viewDto: FreightDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val writeReq = WriteRequest(
                    authLevel = vmData.permissionLevel,
                    data = generateDto(viewDto)
                )
                useCase.save(writeReq)
                emit(Response.Success())
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(e))
            }
        }

    private fun generateDto(viewDto: FreightDto): FreightDto {
        fun setCommonFields() {
            viewDto.masterUid = vmData.masterUid
            viewDto.truckId = vmData.truckId
            viewDto.employeeId = vmData.driverId
            viewDto.travelId = vmData.travelId
        }

        fun whenEditing(): FreightDto {
            val freight = (data.value!!.freight!!)
            setCommonFields()
            return viewDto.also { dto ->
                dto.id = freight.id
                dto.loadingDate = freight.loadingDate.toDate()
                dto.isCommissionPaid = freight.isCommissionPaid
                dto.commissionPercentual = freight.commissionPercentual.toDouble()
                dto.isValid = freight.isValid
            }
        }

        fun whenCreating(): FreightDto {
            setCommonFields()
            return viewDto.also { dto ->
                dto.masterUid = vmData.masterUid
                dto.truckId = vmData.truckId
                dto.employeeId = vmData.driverId
                dto.travelId = vmData.travelId
                dto.loadingDate = date.value!!.toDate()
                dto.isCommissionPaid = false
                dto.commissionPercentual = vmData.commissionPercentual.toDouble()
                dto.isValid = false
            }
        }

        return if (isEditing) whenEditing()
        else whenCreating()

    }

    fun validateCustomer(customer: String): Boolean {
        var isValid = true

        if (customer.isEmpty()) isValid = false

        val customers = data.value!!.customerList.map { it.name }
        if (!customers.contains(customer)) isValid = false

        return isValid
    }

    fun getCustomerId(customer: String): String {
        return data.value!!.customerList.first { it.name == customer }.id!!
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
    val permissionLevel: AccessLevel
)