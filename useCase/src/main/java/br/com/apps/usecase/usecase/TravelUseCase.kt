package br.com.apps.usecase.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.model.user.AccessLevel
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.repository.travel_aid.TravelAidRepository
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import br.com.apps.usecase.util.awaitData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.InvalidParameterException

class TravelUseCase(
    private val repository: TravelRepository,
    private val freightRepository: FreightRepository,
    private val refuelRepository: RefuelRepository,
    private val expendRepository: OutlayRepository,
    private val aidRepository: TravelAidRepository,
) : CredentialsValidatorI<TravelDto> {

    /**
     * This function allows merging lists of freights, expenditures, and refuels into the corresponding travels.
     *
     * @param travelList The list of travels into which additional data will be merged.
     * @param freightList The list of freights to merge into the travels. Defaults to null.
     * @param outlayList The list of expenditures to merge into the travels. Defaults to null.
     * @param refuelList The list of refuels to merge into the travels. Defaults to null.
     */
    fun mergeTravelData(
        travelList: List<Travel>,
        freightList: List<Freight>? = null,
        outlayList: List<Outlay>? = null,
        refuelList: List<Refuel>? = null,
        aidList: List<TravelAid>? = null
    ) {
        freightList?.let {
            travelList.forEach { travel ->
                val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
                val freights = freightList.filter { it.travelId == travelId }
                travel.freights = freights
            }
        }
        outlayList?.let {
            travelList.forEach { travel ->
                val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
                val expends = outlayList.filter { it.travelId == travelId }
                travel.expends = expends
            }
        }
        refuelList?.let {
            travelList.forEach { travel ->
                val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
                val refuels = refuelList.filter { it.travelId == travelId }
                travel.refuels = refuels
            }
        }
        aidList?.let {
            travelList.forEach { travel ->
                val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
                val aid = aidList.filter { it.travelId == travelId }
                travel.aids = aid
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
            val liters = travels.flatMap { it.refuels!! }.sumOf { it.amountLiters }

            val distance = finalOdometer?.subtract(initialOdometer)

            distance?.divide(liters, 2, RoundingMode.HALF_EVEN) ?: BigDecimal.ZERO
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
                    repository.fetchTravelById(travelId).awaitData()
                }
                val freightsResp = async {
                    freightRepository.fetchFreightListByTravelId(travelId).awaitData()
                }
                val refuelsResp = async {
                    refuelRepository.fetchRefuelListByTravelId(travelId).awaitData()
                }
                val expendsResp = async {
                    expendRepository.fetchOutlayListByTravelId(travelId).awaitData()
                }
                val aidsResp = async {
                    aidRepository.fetchTravelAidListByTravelId(travelId).awaitData()
                }

                val travel = travelResp.await().also {
                    it?.freights = freightsResp.await()
                    it?.refuels = refuelsResp.await()
                    it?.expends = expendsResp.await()
                    it?.aids = aidsResp.await()
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
                val travels = repository.fetchTravelListByDriverId(driverId).awaitData()
                val ids = travels!!.mapNotNull { it.id }

                val freightsDef = async {
                    freightRepository.fetchFreightListByTravelIds(ids).awaitData()
                }
                val refuelsDef = async {
                    refuelRepository.fetchRefuelListByTravelIds(ids).awaitData()
                }
                val expendsDef = async {
                    expendRepository.fetchOutlayListByTravelIds(ids).awaitData()
                }
                val aidsDef = async {
                    aidRepository.fetchTravelAidListByTravelIds(ids).awaitData()
                }

                mergeTravelData(
                    travelList = travels!!,
                    freightList = freightsDef.await(),
                    refuelList = refuelsDef.await(),
                    outlayList = expendsDef.await(),
                    aidList = aidsDef.await()
                )

                return@coroutineScope MutableLiveData(Response.Success(travels))

            } catch (e: Exception) {
                return@coroutineScope MutableLiveData(Response.Error(e))
            }
        }
    }

    suspend fun getTravelListByDriverIdAndIsFinished(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<Travel>>> {
        return coroutineScope {
            try {
                val travels = repository.fetchTravelListByDriverIdAndIsFinished(driverId).awaitData()
                val ids = travels!!.mapNotNull { it.id }

                val freightsDef = async {
                    freightRepository.fetchFreightListByTravelIds(ids).awaitData()
                }
                val refuelsDef = async {
                    refuelRepository.fetchRefuelListByTravelIds(ids).awaitData()
                }
                val expendsDef = async {
                    expendRepository.fetchOutlayListByTravelIds(ids).awaitData()
                }
                val aidsDef = async {
                    aidRepository.fetchTravelAidListByTravelIds(ids).awaitData()
                }

                mergeTravelData(
                    travelList = travels,
                    freightList = freightsDef.await(),
                    refuelList = refuelsDef.await(),
                    outlayList = expendsDef.await(),
                    aidList = aidsDef.await()
                )

                return@coroutineScope MutableLiveData(Response.Success(travels))

            } catch (e: Exception) {
                return@coroutineScope MutableLiveData(Response.Error(e))
            }
        }
    }

    suspend fun createANewTravel() {
        coroutineScope {


        }
    }

    /**
     * delete
     */
    suspend fun deleteTravel(travel: Travel) {
        coroutineScope {
            if (!travel.isDeletable()) throw InvalidParameterException("This travel cannot be deleted")
            travel.freights?.forEach { it.id?.let { id -> freightRepository.delete(id) } }
            travel.refuels?.forEach { it.id?.let { id -> refuelRepository.delete(id) } }
            travel.expends?.forEach { it.id?.let { id -> expendRepository.delete(id) } }
            repository.delete(travel.id!!)
        }

    }

    suspend fun setTravelFinished(permission: AccessLevel, dto: TravelDto) {
        dto.validateDataForDbInsertion()
        repository.save(dto)
    }

    override fun validatePermission(permission: AccessLevel, dto: TravelDto) {
        dto.isFinished?.let { isFinished ->
            if (isFinished && permission != AccessLevel.MANAGER)
                throw InvalidParameterException("Invalid credentials for $permission")
        } ?: throw NullPointerException("Validation is null")
    }

    fun getExpendListWitchIsNotRefundYet(travelList: List<Travel>): List<Outlay> {
        return travelList
            .flatMap { it.expends ?: emptyList() }
            .filter { expend ->
                expend.isPaidByEmployee &&
                !expend.isAlreadyRefunded &&
                expend.isValid
            }
    }

    fun getFreightListWitchIsNotPaidYet(travelList: List<Travel>): List<Freight> {
        return travelList
            .flatMap { it.freights ?: emptyList() }
            .filter { freight ->
                freight.isValid &&
                !freight.isCommissionPaid
            }
    }

    fun getTravelAidListWitchIsNotRefundYet(travelList: List<Travel>): List<TravelAid> {
        return travelList
            .flatMap { it.aids ?: emptyList() }
            .filter { aid -> !aid.isPaid }
    }

}

