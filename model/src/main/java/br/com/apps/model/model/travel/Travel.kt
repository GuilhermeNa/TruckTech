package br.com.apps.model.model.travel

import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.exceptions.DateOrderException
import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.OdometerOrderException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.InvalidParameterException
import java.time.LocalDateTime

/**
 * This class encapsulates details of a travel record for a truck, including associated expenses, refuels, freights and aids.
 *
 * Notes:
 * * A travel record is associated with a specific [Truck] and [Employee].
 * * The travel record can include multiple associated [Freight]s, [Refuel]s, [Outlay]s, and [TravelAid]s.
 *
 * @property masterUid Unique identifier for the master record associated with this travel.
 * @property id Optional unique identifier for the [Travel]. If not provided, it will be generated automatically.
 * @property truckId Identifier for the [Truck] involved in this travel.
 * @property employeeId Identifier for the [Employee] responsible for this travel.
 * @property initialDate Date and time when the travel started.
 * @property finalDate Optional date and time when the travel ended. If not provided, the travel is ongoing.
 * @property initialOdometerMeasurement Odometer reading at the start of the travel.
 * @property finalOdometerMeasurement Optional odometer reading at the end of the travel. If not provided, it is not yet concluded.
 * @property freights List of [Freight]s associated with this travel. Represents the cargo shipments during the journey.
 * @property refuels List of [Refuel]s associated with this travel. Represents fuel transactions that occurred during the journey.
 * @property expends List of [Outlay]s associated with this travel. Represents expenditures incurred during the journey.
 * @property aids List of [TravelAid]s associated with this travel. Represents any advance payments made for the journey.
 * @property isClosed Indicates whether the travel record has been closed (true) or not (false). Once closed, it cannot be modified.
 * @property isFinished Indicates whether the travel has been completed (true) or not (false). Used to determine if all travel activities have concluded.
 */
data class Travel(
    val masterUid: String,
    var id: String? = null,
    val truckId: String,
    val employeeId: String,

    var initialDate: LocalDateTime,
    var finalDate: LocalDateTime? = null,
    var initialOdometerMeasurement: BigDecimal,
    var finalOdometerMeasurement: BigDecimal? = null,

    var freights: List<Freight>? = null,
    var refuels: List<Refuel>? = null,
    var expends: List<Outlay>? = null,
    var aids: List<TravelAid>? = null,

    @field:JvmField var isClosed: Boolean,
    @field:JvmField var isFinished: Boolean

) : ModelObjectInterface<TravelDto> {

    /**
     * Retrieves the size of the specified list associated with the travel.
     *
     * @param listTag The tag indicating the type of list (FREIGHT, EXPEND, REFUEL, AID).
     * @return The size of the specified list.
     * @throws InvalidParameterException If an invalid list tag is provided.
     */
    fun getListSize(listTag: Int): Int = when (listTag) {
        FREIGHT -> freights?.size ?: 0
        EXPEND -> expends?.size ?: 0
        REFUEL -> refuels?.size ?: 0
        AID -> aids?.size ?: 0
        else -> throw InvalidParameterException("Wrong tag for getListSize: ($listTag) ")
    }

    /**
     * Retrieves a list of ids associated with the specified list type.
     *
     * @param listTag The tag indicating the type of list (FREIGHT, EXPEND, REFUEL, AID).
     * @return The list of identifiers associated with the specified list type.
     * @throws InvalidParameterException If an invalid list tag is provided.
     */
    fun getListOfIdsForList(listTag: Int): List<String> = when (listTag) {
        FREIGHT -> freights?.mapNotNull { it.id } ?: emptyList()
        EXPEND -> expends?.mapNotNull { it.id } ?: emptyList()
        REFUEL -> refuels?.mapNotNull { it.id } ?: emptyList()
        AID -> aids?.mapNotNull { it.id } ?: emptyList()
        else -> throw InvalidParameterException("Invalid tag for ($listTag)")
    }

    /**
     * Calculates and returns the total commission value for all freights in the travel.
     *
     * @return The total commission value as a BigDecimal.
     */
    fun getCommissionValue(): BigDecimal {
        return freights?.sumOf { it.getCommissionValue() } ?: BigDecimal.ZERO
    }

    /**
     * Calculates and returns the difference between the initial and final odometer measurements.
     *
     * @return The difference between the initial and final odometer measurements as a BigDecimal.
     */
    fun getDifferenceBetweenInitialAndFinalOdometerMeasure(): BigDecimal =
        finalOdometerMeasurement?.let {
            finalOdometerMeasurement!!.subtract(initialOdometerMeasurement)
        } ?: BigDecimal.ZERO

    /**
     * Calculates and returns the total value for the specified list type associated with the travel.
     *
     * @param listTag The tag indicating the type of list (FREIGHT, EXPEND, REFUEL, AID).
     * @return The total value for the specified list type as a BigDecimal.
     * @throws InvalidParameterException If an invalid list tag is provided.
     */
    fun getListTotalValue(listTag: Int): BigDecimal = when (listTag) {
        FREIGHT -> freights?.map { it.value }?.sumOf { it } ?: BigDecimal.ZERO
        EXPEND -> expends?.map { it.value }?.sumOf { it } ?: BigDecimal.ZERO
        REFUEL -> refuels?.map { it.totalValue }?.sumOf { it } ?: BigDecimal.ZERO
        AID -> aids?.map { it.value }?.sumOf { it } ?: BigDecimal.ZERO
        else -> throw InvalidParameterException("Invalid tag for ($listTag)")
    }

    /**
     * Calculates and returns the liquid value (total freight value minus refuel, expenses, and commission values).
     *
     * @return The liquid value as a BigDecimal.
     */
    fun getLiquidValue(): BigDecimal {
        val freight = getListTotalValue(FREIGHT)
        val commission = freights?.sumOf { it.getCommissionValue() } ?: BigDecimal.ZERO
        val refuel = getListTotalValue(REFUEL)
        val expend = getListTotalValue(EXPEND)
        return freight.subtract(refuel).subtract(expend).subtract(commission)
            .setScale(2, RoundingMode.HALF_EVEN)
    }

    /**
     * Calculates and returns the travel authentication percentage based on authenticated items.
     *
     * @return The travel authentication percentage as a Double.
     */
    fun getTravelAuthenticationPercent(): Double {
        val authenticableItems = getListSize(FREIGHT) + getListSize(EXPEND) + getListSize(REFUEL)
        var authenticated = 0.0

        freights?.forEach { if (it.isValid) authenticated++ }
        refuels?.forEach { if (it.isValid) authenticated++ }
        expends?.forEach { if (it.isValid) authenticated++ }

        return authenticated.div(authenticableItems.toDouble()) * 100
    }

    /**
     * Checks if the travel is ready to be finished. It happen when the travel is 100 validated.
     *
     * @return True if the travel is ready to be finished, false otherwise.
     */
    fun isReadyToBeFinished(): Boolean {
        return getTravelAuthenticationPercent() == 100.0 && !freights.isNullOrEmpty()
    }

    /**
     * Checks if the travel is empty (no freights, expenses, refuels, or aids).
     *
     * @return True if the travel is empty, false otherwise.
     */
    fun isEmptyTravel(): Boolean {
        return getListSize(FREIGHT) + getListSize(EXPEND) + getListSize(REFUEL) + getListSize(AID) == 0
    }

    /**
     * Validates the travel data for saving by checking if all required data is present and valid.
     *
     * @throws EmptyDataException If no freights are found in the travel.
     * @throws InvalidParameterException If any freight, refuel, or expense is not valid.
     * @throws DateOrderException If the dates are in incorrect order.
     * @throws DuplicatedItemsException If there are duplicated items in any list.
     * @throws OdometerOrderException If the odometer measurements are in incorrect order.
     * @throws NullPointerException If there is a failure to load freights.
     */
    fun validateForSaving() {
        freights?.also {
            if (it.isEmpty()) throw EmptyDataException("Nenhuma viagem encontrada")
            it.forEach { f ->
                if (!f.isValid) throw InvalidParameterException("Frete não validado")
            }
        } ?: throw NullPointerException("Falha ao carregar fretes")

        refuels?.forEach { r ->
            if (!r.isValid) throw InvalidParameterException("Abastecimento não validado")
        }

        expends?.forEach { e ->
            if (!e.isValid) throw InvalidParameterException("Despesa não validada")
        }

        validateDatesOrder()
        thereIsDuplicatedItems()
        validateOdometerMeasures()
    }

    private fun validateDatesOrder() {
        finalDate?.let {
            if (initialDate.isAfter(finalDate)) throw DateOrderException("Dates in wrong order")
        } ?: throw NullPointerException("Final date is null")
    }

    private fun validateOdometerMeasures() {
        finalOdometerMeasurement?.let {
            if (initialOdometerMeasurement >
                finalOdometerMeasurement
            ) throw OdometerOrderException("Incorrect odometer measure: final cannot be higher than initial measure")
        }
    }

    private fun thereIsDuplicatedItems() {
        freights?.let { freights ->
            val uniqueIds = mutableSetOf<String>()

            freights.mapNotNull { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    throw DuplicatedItemsException("There is duplicated itens for travel freight list")
                } else {
                    uniqueIds.add(id)
                }
            }
        }

        refuels?.let { refuels ->
            val uniqueIds = mutableSetOf<String>()

            refuels.mapNotNull { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    throw DuplicatedItemsException("There is duplicated itens for travel refuel list")
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        expends?.let { expends ->
            val uniqueIds = mutableSetOf<String>()

            expends.mapNotNull { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    throw DuplicatedItemsException("There is duplicated itens for travel expend list")
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        aids?.let { aids ->
            val uniqueIds = mutableSetOf<String>()

            aids.mapNotNull { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    throw DuplicatedItemsException("There is duplicated itens for travel aid list")
                } else {
                    uniqueIds.add(id)
                }
            }

        }

    }

    /**
     * Checks if the travel can be deleted.
     *
     * @return True if the travel can be deleted, false otherwise.
     */
    fun isDeletable(): Boolean {
        if (isFinished) return false
        if (id == null) return false

        freights?.let {
            it.forEach { f ->
                if (f.isValid) return false
                if (f.id == null) return false
            }
        }

        refuels?.let {
            it.forEach { r ->
                if (r.isValid) return false
                if (r.id == null) return false
            }
        }

        expends?.let {
            it.forEach { e ->
                if (e.isValid) return false
                if (e.id == null) return false
            }
        }

        aids?.let {
            if (it.isNotEmpty()) return false
        }

        return true
    }

    /**
     * Calculates and returns the profit percentage based on the total profit and total waste.
     *
     * @return The profit percentage as a BigDecimal.
     */
    fun getProfitPercent(): BigDecimal {
        val profit = getListTotalValue(FREIGHT)
        val waste =
            getListTotalValue(REFUEL) +
                    getListTotalValue(EXPEND) +
                    getCommissionValue()

        return if (profit != BigDecimal.ZERO && waste != BigDecimal.ZERO)
            waste
                .divide(profit, 2, RoundingMode.HALF_EVEN)
                .subtract(BigDecimal(1.0))
                .multiply(BigDecimal(100.0))
                .abs()
        else BigDecimal.ZERO

    }

    /**
     * Calculates and returns the fuel average based on the refuels and odometer measurements.
     *
     * @return The fuel average as a BigDecimal.
     */
    fun getFuelAverage(): BigDecimal {
        return refuels?.let { refuels ->
            val liters = refuels.sumOf { it.amountLiters }
            val distance = getDifferenceBetweenInitialAndFinalOdometerMeasure()
            distance.divide(liters, 2, RoundingMode.HALF_EVEN)
        } ?: BigDecimal.ZERO
    }

    /**
     * Checks if average fuel consumption should be considered based on the refuels list.
     *
     * @return True if average fuel consumption should be considered, false otherwise.
     */
    fun shouldConsiderAverage(): Boolean {
        if (refuels.isNullOrEmpty()) return false
        return refuels!!.last().isCompleteRefuel
    }

    companion object {
        const val FREIGHT = 0
        const val EXPEND = 1
        const val REFUEL = 2
        const val AID = 3
    }

    override fun toDto() = TravelDto(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        employeeId = this.employeeId,
        initialDate = this.initialDate.toDate(),
        finalDate = this.finalDate?.toDate(),
        initialOdometerMeasurement = this.initialOdometerMeasurement.toDouble(),
        finalOdometerMeasurement = this.finalOdometerMeasurement?.toDouble(),
        isFinished = this.isFinished,
        isClosed = this.isClosed
    )

}

data class PerformanceItem(
    val title: String,
    val meta: String,
    val hit: String,
    val percent: String,
    var progressBar: Int
)