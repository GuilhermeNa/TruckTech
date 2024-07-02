package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.repository.travel_aid.TravelAidRepository
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.usecase.util.awaitData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.InvalidParameterException

class TravelUseCase(
    private val repository: TravelRepository,
    private val freightRepository: FreightRepository,
    private val refuelRepository: RefuelRepository,
    private val expendRepository: ExpendRepository,
    private val aidRepository: TravelAidRepository,
) : CredentialsValidatorI<TravelDto> {

    /**
     * This function allows merging lists of freights, expenditures, and refuels into the corresponding travels.
     *
     * @param travelList The list of travels into which additional data will be merged.
     * @param freightList The list of freights to merge into the travels. Defaults to null.
     * @param expendList The list of expenditures to merge into the travels. Defaults to null.
     * @param refuelList The list of refuels to merge into the travels. Defaults to null.
     */
    fun mergeTravelData(
        travelList: List<Travel>,
        freightList: List<Freight>? = null,
        expendList: List<Expend>? = null,
        refuelList: List<Refuel>? = null,
        aidList: List<TravelAid>? = null
    ) {
        freightList?.let {
            travelList.forEach { travel ->
                val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
                val freights = freightList.filter { it.travelId == travelId }
                travel.freightsList = freights
            }
        }
        expendList?.let {
            travelList.forEach { travel ->
                val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
                val expends = expendList.filter { it.travelId == travelId }
                travel.expendsList = expends
            }
        }
        refuelList?.let {
            travelList.forEach { travel ->
                val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
                val refuels = refuelList.filter { it.travelId == travelId }
                travel.refuelsList = refuels
            }
        }
        aidList?.let {
            travelList.forEach { travel ->
                val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
                val aid = aidList.filter { it.travelId == travelId }
                travel.aidList = aid
            }
        }
    }

    /**
     * Calculates the profit percentage based on the provided list of travels.
     * Profit percentage is calculated as the ratio of total waste (expenditures and refuels)
     * to total profit (freight earnings).
     *
     * @param travelList The list of travels for which the profit percentage needs to be calculated.
     * @return The profit percentage as a BigDecimal value.
     */
    fun getProfitPercentage(travels: List<Travel>): BigDecimal {
        val total = travels.sumOf { it.getProfitPercent() }
        return total.divide(BigDecimal(travels.size), 2, RoundingMode.HALF_EVEN)
    }

    /**
     * Calculates the average refuel cost per kilometer traveled based on the provided list of travels.
     *
     * @param travelList The list of travels for which the refuel average needs to be calculated.
     * @return The average refuel cost per kilometer as a BigDecimal value.
     */
    fun getRefuelAverage(travels: List<Travel>): BigDecimal {
        return if (travels.size == 1)
            travels[0].getFuelAverage()
        else {
            val initialOdometer = travels.last().initialOdometerMeasurement
            val finalOdometer = travels.first().finalOdometerMeasurement
            val liters = travels.flatMap { it.refuelsList!! }.sumOf { it.amountLiters }

            val distance = finalOdometer?.subtract(initialOdometer)

            distance?.divide(liters, 2, RoundingMode.HALF_EVEN) ?: BigDecimal.ZERO
        }
    }

    /**
     * Retrieves a complete list of travels associated with a driver ID.
     * The function merges data from different repositories, such as travel, freight, refuel, and expend lists.
     *
     * @param driverId The ID of the driver for whom to retrieve the travel list.
     * @return A LiveData object that emits responses containing the complete travel list.
     */
    suspend fun getCompleteTravelListByDriverId(driverId: String): LiveData<Response<List<Travel>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Travel>>>()

            CoroutineScope(Dispatchers.Main).launch {
                val deferredA = CompletableDeferred<List<Travel>>()
                val liveDataA = repository.getTravelListByDriverId(driverId)
                mediator.addSource(liveDataA) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let { deferredA.complete(it) }
                    }
                }

                val travels = deferredA.await()
                val travelIdList = travels.mapNotNull { t -> t.id }

                val deferredB = CompletableDeferred<List<Freight>>()
                val liveDataB = freightRepository.getFreightListByTravelIds(travelIdList)
                mediator.addSource(liveDataB) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let { deferredB.complete(it) }
                    }
                }

                val deferredC = CompletableDeferred<List<Refuel>>()
                val liveDataC = refuelRepository.getRefuelListByTravelIds(travelIdList)
                mediator.addSource(liveDataC) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let { deferredC.complete(it) }
                    }
                }

                val deferredD = CompletableDeferred<List<Expend>>()
                val liveDataD = expendRepository.getExpendListByTravelIds(travelIdList)
                mediator.addSource(liveDataD) { response ->
                    when (response) {
                        is Response.Error -> mediator.value = response
                        is Response.Success -> response.data?.let { deferredD.complete(it) }
                    }
                }

                val freightList = deferredB.await()
                val refuelList = deferredC.await()
                val expendList = deferredD.await()

                mergeTravelData(travels, freightList, expendList, refuelList)
                mediator.value = Response.Success(data = travels)
            }

            return@coroutineScope mediator
        }
    }

    /**
     * Retrieves a complete list of travels associated with a travel ID.
     * The function merges data from different repositories, such as travel, freight, refuel, and expend lists.
     *
     * @param travelId The ID of the driver for whom to retrieve the travel list.
     * @return A LiveData object that emits responses containing the complete travel.
     */
    suspend fun getTravelById(travelId: String, flow: Boolean = false): LiveData<Response<Travel>> {
        return coroutineScope {
            try {

                val travelResp = async {
                    repository.getTravelById(travelId).awaitData()
                }
                val freightsResp = async {
                    freightRepository.getFreightListByTravelId(travelId).awaitData()
                }
                val refuelsResp = async {
                    refuelRepository.getRefuelListByTravelId(travelId).awaitData()
                }
                val expendsResp = async {
                    expendRepository.getExpendListByTravelId(travelId).awaitData()
                }
                val aidsResp = async {
                    aidRepository.getTravelAidListByTravelId(travelId).awaitData()
                }

                val travel = travelResp.await().also {
                    it.freightsList = freightsResp.await()
                    it.refuelsList = refuelsResp.await()
                    it.expendsList = expendsResp.await()
                    it.aidList = aidsResp.await()
                }

                return@coroutineScope MutableLiveData(Response.Success(travel))

            } catch (e: Exception) {
                return@coroutineScope MutableLiveData(Response.Error(e))

            }
        }
    }

    /**
     * Retrieves a complete list of travels associated with a driver ID.
     * The function merges data from different repositories, such as travel, freight, refuel, and expend lists.
     *
     * @param driverId The ID of the driver for whom to retrieve the travel list.
     * @return A LiveData object that emits responses containing the complete travel list.
     */
    suspend fun getTravelListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Travel>>> {
        return coroutineScope {
            try {
                val travels = repository.getTravelListByDriverId(driverId).awaitData()
                val ids = travels.mapNotNull { it.id }

                val freightsDef = async {
                    freightRepository.getFreightListByTravelIds(ids).awaitData()
                }
                val refuelsDef = async {
                    refuelRepository.getRefuelListByTravelIds(ids).awaitData()
                }
                val expendsDef = async {
                    expendRepository.getExpendListByTravelIds(ids).awaitData()
                }
                val aidsDef = async {
                    aidRepository.getTravelAidListByTravelIds(ids).awaitData()
                }

                mergeTravelData(
                    travelList = travels,
                    freightList = freightsDef.await(),
                    refuelList = refuelsDef.await(),
                    expendList = expendsDef.await(),
                    aidList = aidsDef.await()
                )

                return@coroutineScope MutableLiveData(Response.Success(travels))

            } catch (e: Exception) {
                return@coroutineScope MutableLiveData(Response.Error(e))
            }
        }
    }

    /**
     * delete
     */
    suspend fun deleteTravel(travel: Travel) {
        coroutineScope {
            if (!travel.isDeletable()) throw InvalidParameterException("This travel cannot be deleted")
            travel.freightsList?.forEach { it.id?.let { id -> freightRepository.delete(id) } }
            travel.refuelsList?.forEach { it.id?.let { id -> refuelRepository.delete(id) } }
            travel.expendsList?.forEach { it.id?.let { id -> expendRepository.delete(id) } }
            repository.delete(travel.id!!)
        }

    }

    suspend fun setTravelFinished(permission: PermissionLevelType, dto: TravelDto) {
        if (!dto.validateFieldsForFinish()) throw InvalidParameterException("Invalid Travel for finish")
        repository.save(dto)
    }

    override fun validatePermission(permission: PermissionLevelType, dto: TravelDto) {
        dto.isFinished?.let { isFinished ->
            if (isFinished && permission != PermissionLevelType.MANAGER)
                throw InvalidParameterException("Invalid credentials for $permission")
        } ?: throw NullPointerException("Validation is null")
    }

}

