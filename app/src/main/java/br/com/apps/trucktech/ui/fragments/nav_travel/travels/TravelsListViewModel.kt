package br.com.apps.trucktech.ui.fragments.nav_travel.travels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.factory.TravelFactory
import br.com.apps.model.factory.TravelFactoryData
import br.com.apps.model.mapper.toDto
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.TravelIdsData
import br.com.apps.usecase.TravelUseCase
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TravelsListViewModel(
    private val idHolder: IdHolder,
    private val useCase: TravelUseCase,
    private val repository: TravelRepository
) : ViewModel() {

    val masterUid =
        idHolder.masterUid ?: throw NullPointerException("TravelsListViewModel: masterUid null")
    val driverId =
        idHolder.driverId ?: throw NullPointerException("TravelsListViewModel: driverId null")
    val truckId =
        idHolder.truckId ?: throw NullPointerException("TravelsListViewModel: truckId null")

    /**
     * LiveData holding the response data of type [Response] with a list of [Travel]
     * to be displayed on screen.
     */
    private val _travelData = MutableLiveData<Response<List<Travel>>>()
    val travelData get() = _travelData

    /**
     * LiveData with a dark layer state, used when dialog is requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    /**
     * LiveData with a bottom nav state, used when dialog is requested.
     */
    private var _bottomNav = MutableLiveData(true)
    val bottomNav get() = _bottomNav

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            useCase.getCompleteTravelListByDriverId(driverId).asFlow().collect {
                _travelData.postValue(it)
            }
        }
    }

    suspend fun delete(idsData: TravelIdsData) =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                useCase.deleteTravel(idsData)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    /**
     * Sets the visibility of the [_darkLayer] to true, indicating that it should be shown.
     */
    fun requestDarkLayer() {
        _darkLayer.value = true
    }

    /**
     * Sets the visibility of the [_darkLayer] to false, indicating that it should be dismissed.
     */
    fun dismissDarkLayer() {
        _darkLayer.value = false
    }

    /**
     * Sets the visibility of the [_bottomNav] to true, indicating that it should be shown.
     */
    fun requestBottomNav() {
        _bottomNav.value = true
    }

    /**
     * Sets the visibility of the [_bottomNav] to false, indicating that it should be dismissed.
     */
    fun dismissBottomNav() {
        _bottomNav.value = false
    }

    fun createNewTravel() = liveData<Response<Unit>>(viewModelScope.coroutineContext) {
        try {
            val dto = getTravelDto()
            repository.save(dto)
            emit(Response.Success())
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }

    private fun getTravelDto(): TravelDto {
        val travelFactoryData = TravelFactoryData(
            masterUid = idHolder.masterUid,
            truckId = idHolder.truckId,
            driverId = idHolder.driverId,
            isFinished = false.toString(),
            initialDate = LocalDateTime.now().toString()
        )
        return TravelFactory.create(travelFactoryData).toDto()
    }

}

