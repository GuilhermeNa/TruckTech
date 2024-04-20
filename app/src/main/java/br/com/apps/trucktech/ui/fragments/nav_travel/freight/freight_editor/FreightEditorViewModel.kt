package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.factory.FreightFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Freight.Companion.TAG_CARGO
import br.com.apps.model.model.travel.Freight.Companion.TAG_COMPANY
import br.com.apps.model.model.travel.Freight.Companion.TAG_DESTINY
import br.com.apps.model.model.travel.Freight.Companion.TAG_ORIGIN
import br.com.apps.model.model.travel.Freight.Companion.TAG_VALUE
import br.com.apps.model.model.travel.Freight.Companion.TAG_WEIGHT
import br.com.apps.repository.Response
import br.com.apps.repository.repository.FreightRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class FreightEditorViewModel(
    private val idHolder: IdHolder,
    private val repository: FreightRepository
) : ViewModel() {

    /**
     * The [Freight] to be loaded when there is an ID.
     */
    lateinit var freight: Freight

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
    private val _freightData = MutableLiveData<Response<Freight>>()
    val freightData get() = _freightData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        if (idHolder.freightId != null) {
            loadData(idHolder.freightId!!)
        } else {
            _date.value = LocalDateTime.now()
        }
    }

    /**
     * Loads data asynchronously and set [_freightData] response.
     *
     * @param [freightId] is the id of the freight to be loaded
     */
    private fun loadData(freightId: String) {
        viewModelScope.launch {
            repository.getFreightById(freightId, false).asFlow().collect { response ->
                _freightData.value =
                    when (response) {
                        is Response.Error -> response
                        is Response.Success -> {
                            response.data?.let {
                                freight = it
                                _date.value = it.loadingDate!!
                            }
                            response
                        }
                    }
            }
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
     * Constructs and returns an [FreightDto] based on the provided mapped fields.
     *
     * @param mappedFields A HashMap containing the mapped fields.
     * @return An [FreightDto] object.
     */
    fun getFreightDto(mappedFields: HashMap<String, String>): FreightDto {
        return if (::freight.isInitialized) {
            freight.updateFields(mappedFields)
            freight.toDto()
        } else {
            FreightFactory.createDto(
                nMasterId = idHolder.masterUid,
                nTruckId = idHolder.truckId,
                nTravelId = idHolder.travelId,
                nOrigin = mappedFields[TAG_ORIGIN],
                nCompany = mappedFields[TAG_COMPANY],
                nDestiny = mappedFields[TAG_DESTINY],
                nWeight = mappedFields[TAG_WEIGHT],
                nCargo = mappedFields[TAG_CARGO],
                nValue = mappedFields[TAG_VALUE],
                nLoadingDate = date.value
            )
        }
    }

    /**
     * Send the [Freight] to be created or updated.
     */
    fun save(dto: FreightDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                repository.save(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }
}