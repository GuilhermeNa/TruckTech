package br.com.apps.usecase.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.repository.travel_aid.TravelAidRepository
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
            val initialOdometer = travels.last().initialOdometer
            val finalOdometer = travels.first().finalOdometer
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

                val travel = travelResp.await()?.apply {
                    freightsResp.await()?.let { addAllFreights(it) }
                    refuelsResp.await()?.let { addAllRefuels(it) }
                    expendsResp.await()?.let { addAllOutlays(it) }
                    aidsResp.await()?.let { addAllAids(it) }
                }

                return@coroutineScope MutableLiveData(Response.Success(travel))

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
            travel.freights.forEach { it.id.let { id -> freightRepository.delete(id) } }
            travel.refuels.forEach { it.id.let { id -> refuelRepository.delete(id) } }
            travel.outlays.forEach { it.id.let { id -> expendRepository.delete(id) } }
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

}

