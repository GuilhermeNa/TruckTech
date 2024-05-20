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
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.freight.FreightRepository
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
     * Send the [Freight] to be created or updated.
     */
    fun save(mappedFields: HashMap<String, String>) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                val dto = getFreightDto(mappedFields)
                repository.save(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private fun getFreightDto(mappedFields: HashMap<String, String>): FreightDto {
        return if (::freight.isInitialized) {
            FreightFactory.update(freight, mappedFields)
            freight.toDto()
        } else {
            mappedFields[FreightFactory.TAG_MASTER_UID] = idHolder.masterUid!!
            mappedFields[FreightFactory.TAG_TRUCK_ID] = idHolder.truckId!!
            mappedFields[FreightFactory.TAG_DRIVER_ID] = idHolder.driverId!!
            mappedFields[FreightFactory.TAG_TRAVEL_ID] = idHolder.travelId!!
            mappedFields[FreightFactory.TAG_TRAVEL_ID] = idHolder.travelId!!
            FreightFactory.create(mappedFields).toDto()
        }
    }

}