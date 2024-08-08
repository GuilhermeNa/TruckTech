package br.com.apps.model.model.travel

import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.exceptions.DateOrderException
import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.OdometerOrderException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.DUPLICATED_ID
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
 * @property outlays List of [Outlay]s associated with this travel. Represents expenditures incurred during the journey.
 * @property aids List of [TravelAid]s associated with this travel. Represents any advance payments made for the journey.
 * @property isClosed Indicates whether the travel has complete refuels and should be considered to travel average.
 * @property isFinished Indicates whether the travel has been completed (true) or not (false). Used to determine if all travel activities have concluded.
 */
data class Travel(
    val masterUid: String,
    val id: String? = null,
    val truckId: String,
    val employeeId: String,
    val initialDate: LocalDateTime,
    val finalDate: LocalDateTime? = null,
    val initialOdometerMeasurement: BigDecimal,
    val finalOdometerMeasurement: BigDecimal? = null,
    val isClosed: Boolean,
    val isFinished: Boolean,
    private val _freights: MutableList<Freight> = mutableListOf(),
    private val _refuels: MutableList<Refuel> = mutableListOf(),
    private val _outlays: MutableList<Outlay> = mutableListOf(),
    private val _aids: MutableList<TravelAid> = mutableListOf()
) : ModelObjectInterface<TravelDto> {

    val freights: List<Freight>
        get() = _freights

    val refuels: List<Refuel>
        get() = _refuels

    val outlays: List<Outlay>
        get() = _outlays

    val aids: List<TravelAid>
        get() = _aids

    companion object {
        const val FREIGHT = 0
        const val OUTLAY = 1
        const val REFUEL = 2
        const val AID = 3

        fun List<Travel>.merge(
            nFreights: List<Freight>? = null, nRefuels: List<Refuel>? = null,
            nOutlays: List<Outlay>? = null, nAids: List<TravelAid>? = null
        ) {
            this.forEach { t ->
                nFreights?.let { freights ->
                    freights.filter { it.travelId == t.id }.let { t.addAllFreights(it) }
                }
                nRefuels?.let { refuels ->
                    refuels.filter { it.travelId == t.id }.let { t.addAllRefuels(it) }
                }
                nOutlays?.let { outlays ->
                    outlays.filter { it.travelId == t.id }.let { t.addAllOutlays(it) }
                }
                nAids?.let { aids ->
                    aids.filter { it.travelId == t.id }.let { t.addAllAids(it) }
                }
            }
        }

    }

    fun addAllFreights(freights: List<Freight>) {
        val existingIds = _freights.asSequence().map { it.id }.toSet()
        val newFreights = freights.distinctBy { it.id }.filter { it.id !in existingIds }
        _freights.addAll(newFreights)
    }

    fun addAllRefuels(refuels: List<Refuel>) {
        val existingIds = _refuels.asSequence().map { it.id }.toSet()
        val newRefuels = refuels.distinctBy { it.id }.filter { it.id !in existingIds }
        _refuels.addAll(newRefuels)
    }

    fun addAllOutlays(outlays: List<Outlay>) {
        val existingIds = _outlays.asSequence().map { it.id }.toSet()
        val newOutlays = outlays.distinctBy { it.id }.filter { it.id !in existingIds }
        _outlays.addAll(newOutlays)
    }

    fun addAllAids(aids: List<TravelAid>) {
        val existingIds = _aids.asSequence().map { it.id }.toSet()
        val newAids = aids.distinctBy { it.id }.filter { it.id !in existingIds }
        _aids.addAll(newAids)
    }


    /**
     * Retrieves the size of the specified list associated with the travel.
     *
     * @param listTag The tag indicating the type of list (FREIGHT, EXPEND, REFUEL, AID).
     * @return The size of the specified list.
     * @throws InvalidParameterException If an invalid list tag is provided.
     */
    fun getListSize(listTag: Int): Int = when (listTag) {
        FREIGHT -> _freights.size
        OUTLAY -> _outlays.size
        REFUEL -> _refuels.size
        AID -> _aids.size
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
        FREIGHT -> _freights.map { it.id }
        OUTLAY -> _outlays.map { it.id }
        REFUEL -> _refuels.map { it.id }
        AID -> _aids.map { it.id }
        else -> throw InvalidParameterException("Invalid tag for ($listTag)")
    }

    /**
     * Calculates and returns the total commission value for all freights in the travel.
     *
     * @return The total commission value as a BigDecimal.
     */
    fun getCommissionValue(): BigDecimal {
        return _freights.sumOf { it.getCommissionValue() }
    }

    /**
     * Calculates and returns the difference between the initial and final odometer measurements.
     *
     * @return The difference between the initial and final odometer measurements as a BigDecimal.
     */
    fun getDifferenceBetweenInitialAndFinalOdometerMeasure(): BigDecimal =
        finalOdometerMeasurement?.subtract(initialOdometerMeasurement) ?: BigDecimal.ZERO

    /**
     * Calculates and returns the total value for the specified list type associated with the travel.
     *
     * @param listTag The tag indicating the type of list (FREIGHT, EXPEND, REFUEL, AID).
     * @return The total value for the specified list type as a BigDecimal.
     * @throws InvalidParameterException If an invalid list tag is provided.
     */
    fun getListTotalValue(listTag: Int): BigDecimal {
        val value = when (listTag) {
            FREIGHT -> _freights.map { it.value }.sumOf { it }
            OUTLAY -> _outlays.map { it.value }.sumOf { it }
            REFUEL -> _refuels.map { it.totalValue }.sumOf { it }
            AID -> _aids.map { it.value }.sumOf { it }
            else -> throw InvalidParameterException("Invalid tag for ($listTag)")
        }
        return value.setScale(2)
    }

    /**
     * Calculates and returns the liquid value (total freight value minus refuel, expenses, and commission values).
     *
     * @return The liquid value as a BigDecimal.
     */
    fun getLiquidValue(): BigDecimal {
        val freight = getListTotalValue(FREIGHT)
        val commission = _freights.sumOf { it.getCommissionValue() }
        val refuel = getListTotalValue(REFUEL)
        val expend = getListTotalValue(OUTLAY)
        return freight.subtract(refuel).subtract(expend).subtract(commission)
            .setScale(2, RoundingMode.HALF_EVEN)
    }

    /**
     * Calculates and returns the travel authentication percentage based on authenticated items.
     *
     * @return The travel authentication percentage as a Double.
     */
    fun getTravelAuthenticationPercent(): Double {
        val authenticableItems = getListSize(FREIGHT) + getListSize(OUTLAY) + getListSize(REFUEL)
        var authenticated = 0.0

        _freights.forEach { if (it.isValid) authenticated++ }
        _refuels.forEach { if (it.isValid) authenticated++ }
        _outlays.forEach { if (it.isValid) authenticated++ }

        return authenticated.div(authenticableItems.toDouble()) * 100
    }

    /**
     * Checks if the travel is ready to be finished. It happen when the travel is 100 validated.
     *
     * @return True if the travel is ready to be finished, false otherwise.
     */
    fun isReadyToBeFinished(): Boolean {
        return getTravelAuthenticationPercent() == 100.0 && _freights.isNotEmpty()
    }

    /**
     * Checks if the travel is empty (no freights, expenses, refuels, or aids).
     *
     * @return True if the travel is empty, false otherwise.
     */
    fun isEmptyTravel(): Boolean {
        return getListSize(FREIGHT) + getListSize(OUTLAY) + getListSize(REFUEL) + getListSize(AID) == 0
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
        _freights.also {
            if (it.isEmpty()) throw EmptyDataException("Nenhuma viagem encontrada")
            it.forEach { f ->
                if (!f.isValid) throw InvalidParameterException("Frete não validado")
            }
        }

        _refuels.forEach { r ->
            if (!r.isValid) throw InvalidParameterException("Abastecimento não validado")
        }

        _outlays.forEach { e ->
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
        _freights.let { freights ->
            val uniqueIds = mutableSetOf<String>()

            freights.map { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    throw DuplicatedItemsException("There is duplicated itens for travel freight list")
                } else {
                    uniqueIds.add(id)
                }
            }
        }

        _refuels.let { refuels ->
            val uniqueIds = mutableSetOf<String>()

            refuels.map { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    throw DuplicatedItemsException("There is duplicated itens for travel refuel list")
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        _outlays.let { expends ->
            val uniqueIds = mutableSetOf<String>()

            expends.map { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    throw DuplicatedItemsException("There is duplicated itens for travel expend list")
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        _aids.let { aids ->
            val uniqueIds = mutableSetOf<String>()

            aids.map { it.id }.forEach { id ->
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

        _freights.let { it.forEach { f -> if (f.isValid) return false } }

        _refuels.let { it.forEach { r -> if (r.isValid) return false } }

        _outlays.let { it.forEach { e -> if (e.isValid) return false } }

        _aids.let { it.forEach { a -> if (a.isValid) return false } }

        return true
    }

    /**
     * Calculates and returns the profit percentage based on the total profit and total waste.
     *
     * @return The profit percentage as a BigDecimal.
     */
    fun getProfitPercent(): BigDecimal {
        fun calcWaste(): BigDecimal {
            val value = getListTotalValue(REFUEL) +
                    getListTotalValue(OUTLAY) +
                    getCommissionValue()
            return value.setScale(2)
        }

        val profit = getListTotalValue(FREIGHT)
        val waste = calcWaste()

        return when {
            profit == BigDecimal.ZERO.setScale(2) -> BigDecimal.ZERO.setScale(2)

            waste == BigDecimal.ZERO.setScale(2) -> BigDecimal("100.00")

            else -> waste.divide(profit, 2, RoundingMode.HALF_EVEN)
                .subtract(BigDecimal(1.0))
                .multiply(BigDecimal(100.0))
                .abs()
        }

    }

    /**
     * Calculates and returns the fuel average based on the refuels and odometer measurements.
     *
     * @return The fuel average as a BigDecimal.
     */
    fun getFuelAverage(): BigDecimal {
        return _refuels.let { refuels ->
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
        if (_refuels.isEmpty()) return false
        return _refuels.last().isCompleteRefuel
    }

    fun addFreight(freight: Freight) {
        val existingIds = _freights.asSequence().map { it.id }.toSet()
        if (freight.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        _freights.add(freight)
    }

    fun addRefuel(refuel: Refuel) {
        val existingIds = _refuels.asSequence().map { it.id }.toSet()
        if (refuel.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        _refuels.add(refuel)
    }

    fun addOutlay(outlay: Outlay) {
        val existingIds = _outlays.asSequence().map { it.id }.toSet()
        if (outlay.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        _outlays.add(outlay)
    }

    fun addAid(aid: TravelAid) {
        val existingIds = _aids.asSequence().map { it.id }.toSet()
        if (aid.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        _aids.add(aid)
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