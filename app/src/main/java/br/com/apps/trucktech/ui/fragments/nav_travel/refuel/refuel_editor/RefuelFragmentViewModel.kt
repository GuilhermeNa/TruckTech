package br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.factory.RefuelFactory
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Refuel.Companion.TAG_AMOUNT_LITERS
import br.com.apps.model.model.travel.Refuel.Companion.TAG_IS_COMPLETE
import br.com.apps.model.model.travel.Refuel.Companion.TAG_ODOMETER
import br.com.apps.model.model.travel.Refuel.Companion.TAG_STATION
import br.com.apps.model.model.travel.Refuel.Companion.TAG_TOTAL_VALUE
import br.com.apps.model.model.travel.Refuel.Companion.TAG_VALUE_PER_LITER
import br.com.apps.repository.Response
import br.com.apps.repository.repository.RefuelRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class RefuelFragmentViewModel(
    private val idHolder: IdHolder,
    private val repository: RefuelRepository
) : ViewModel() {

    /**
     * The [Refuel] to be loaded when there is an ID.
     */
    lateinit var refuel: Refuel

    /**
     * LiveData containing the [LocalDateTime] of the [Refuel] date,
     * or LocalDateTime.now() if there is no [Refuel] to be loaded.
     */
    private val _date = MutableLiveData<LocalDateTime>()
    val date get() = _date

    /**
     * if there is an [Refuel] to be loaded, this liveData is
     * responsible for holding the [Response] data of the [Refuel].
     */
    private val _refuelData = MutableLiveData<Response<Refuel>>()
    val refuelData get() = _refuelData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        if (idHolder.refuelId != null) {
            loadData(idHolder.refuelId!!)
        } else {
            _date.value = LocalDateTime.now()
        }
    }

    /**
     * Loads data asynchronously and set [_refuelData] response.
     *
     * @param [refuelId] is the id of the refuel to be loaded
     */
    private fun loadData(refuelId: String) {
        viewModelScope.launch {
            repository.getRefuelById(refuelId, false).asFlow().collect { response ->
                _refuelData.value =
                    when (response) {
                        is Response.Error -> response
                        is Response.Success -> {
                            response.data?.let {
                                refuel = it
                                _date.value = it.date!!
                            }
                            response
                        }
                    }
            }
        }
    }

    /**
     * Constructs and returns an [RefuelDto] based on the provided mapped fields.
     *
     * @param mappedFields A HashMap containing the mapped fields.
     * @return An [RefuelDto] object.
     */
    fun getRefuelDto(mappedFields: HashMap<String, String>): RefuelDto {
        return if (::refuel.isInitialized) {
            refuel.updateFields(mappedFields)
            refuel.toDto()
        } else {
            RefuelFactory.createDto(
                nMasterUid = idHolder.masterUid,
                nTravelId = idHolder.travelId,
                nTruckId = idHolder.truckId,
                nDate = date.value,
                nStation = mappedFields[TAG_STATION],
                nOdometer = mappedFields[TAG_ODOMETER],
                nAmountLiters = mappedFields[TAG_AMOUNT_LITERS],
                nValuePerLiter = mappedFields[TAG_VALUE_PER_LITER],
                nTotalValue = mappedFields[TAG_TOTAL_VALUE],
                nIsComplete = mappedFields[TAG_IS_COMPLETE].toBoolean()
            )
        }
    }

    /**
     * Send the [Refuel] to be created or updated.
     */
    fun save(dto: RefuelDto) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                repository.save(dto)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
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

}