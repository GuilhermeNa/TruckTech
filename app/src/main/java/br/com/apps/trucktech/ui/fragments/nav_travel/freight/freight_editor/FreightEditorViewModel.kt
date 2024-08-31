package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.expressions.atBrZone
import br.com.apps.model.model.Customer
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.util.toDate
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.trucktech.service.CustomerService
import br.com.apps.trucktech.service.FreightService
import br.com.apps.trucktech.util.image.Image
import br.com.apps.trucktech.util.image.ImageUseCase
import br.com.apps.usecase.usecase.FreightUseCase
import br.com.apps.usecase.util.buildFreightStoragePath
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(DelicateCoroutinesApi::class)
class FreightEditorViewModel(
    private val vmData: FreightEVMData,
    private val useCase: FreightUseCase,
    private val fService: FreightService,
    private val cService: CustomerService,
    private val storage: StorageRepository,
    private val fRepository: FreightRepository
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

    private val _invoiceImage = MutableLiveData<Image?>()
    val invoiceImage get() = _invoiceImage

    private val _ticketImage = MutableLiveData<Image?>()
    val ticketImage get() = _ticketImage

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
                customers = cService.fetchCustomersByMasterUid(vmData.masterUid)

                val nFreight = vmData.freightId?.let { fService.fetchFreightById(it) }

                sendResponse(nFreight)

            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun sendResponse(freight: Freight?) {
        val data = if (freight == null) {
            setDate(LocalDateTime.now().atBrZone())
            FreightEFData(customerList = customers)
        } else {
            freight.setCustomerById(customers)
            freight.urlInvoice?.run { setInvoice(this) }
            freight.urlTicket?.run { setTicket(this) }
            setDate(freight.loadingDate)
            FreightEFData(customerList = customers, freight = freight)
        }

        setFragmentData(data)
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

                ImageUseCase.checkDirection(
                    isEditing = isEditing,
                    image = _invoiceImage.value,
                    hasPreviousImg = _data.value?.freight?.urlInvoice != null
                ).let { dir ->
                    when (dir) {
                        ImageUseCase.ADDING_WITH_IMAGE -> {
                            writeReq.data.isUpdatingInvoice = true
                            val id = useCase.save(writeReq)
                            GlobalScope.launch {
                                val url =
                                    storage.postImage(invoiceImage.value!!.byteArray!!, buildFreightStoragePath(id))
                                fRepository.updateInvoiceUrl(id, url)
                            }
                        }

                        ImageUseCase.ADDING_WITHOUT_IMAGE -> {
                            writeReq.data.isUpdatingInvoice = false
                            useCase.save(writeReq)
                        }

                        ImageUseCase.EDITING_AND_REPLACING_IMAGE -> {
                            writeReq.data.isUpdatingInvoice = true
                            val id = useCase.save(writeReq)
                            GlobalScope.launch {
                                val url =
                                    storage.postImage(invoiceImage.value!!.byteArray!!, buildFreightStoragePath(id))
                                fRepository.updateInvoiceUrl(id, url)
                            }
                        }

                        ImageUseCase.EDITING_WITHOUT_NEW_IMAGE -> {
                            writeReq.data.isUpdatingInvoice = false
                            useCase.save(writeReq)
                        }

                        ImageUseCase.EDITING_AND_REMOVING_IMAGE -> {
                            writeReq.data.isUpdatingInvoice = false
                            writeReq.data.urlInvoice = null
                            val idToDelete = useCase.save(writeReq)
                            GlobalScope.launch {
                                storage.deleteImage(buildFreightStoragePath(idToDelete))
                            }
                        }

                        ImageUseCase.EDITING_AND_INSERTING_FIRST_IMAGE -> {
                            writeReq.data.isUpdatingInvoice = true
                            val id = useCase.save(writeReq)
                            GlobalScope.launch {
                                val url =
                                    storage.postImage(invoiceImage.value!!.byteArray!!, buildFreightStoragePath(id))
                                fRepository.updateInvoiceUrl(id, url)
                            }
                        }

                        else -> throw NullPointerException()

                    }
                }

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
            viewDto.isUpdatingTicket = false
        }

        fun whenEditing(): FreightDto {
            val freight = (data.value!!.freight!!)
            setCommonFields()
            return viewDto.also { dto ->
                dto.id = freight.id
                dto.loadingDate = freight.loadingDate.toDate()
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
                dto.commissionPercentual = vmData.commissionPercentual.toDouble()
                dto.isValid = false
            }
        }

        return if (isEditing) whenEditing()
        else whenCreating()

    }

    fun validateCustomer(customer: String): Boolean {
        return try {
            if (customer.isEmpty()) return false

            val customers = data.value!!.customerList.map { it.name }
            if (!customers.contains(customer)) return false

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    fun getCustomerId(customer: String): String {
        return data.value!!.customerList.first { it.name == customer }.id
    }

    private fun setInvoice(img: Any?) {
        _invoiceImage.value = try {
            when (img) {
                is ByteArray -> Image(byteArray = img)
                is String -> Image(url = img)
                else -> null
            }
        } catch (e: Exception) {
            null
        }

    }

    private fun setTicket(img: Any?) {
        _ticketImage.value = try {
            when (img) {
                is ByteArray -> Image(byteArray = img)
                is String -> Image(url = img)
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        fService.close()
        cService.close()
    }

    fun setTicketByteArray(ba: ByteArray?) {
        setTicket(ba)
    }

    fun setInvoiceByteArray(ba: ByteArray?) {
        setInvoice(ba)
    }

    fun getInvoiceImage() = _invoiceImage.value?.getData()

    fun getTicketImage() = _ticketImage.value?.getData()

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