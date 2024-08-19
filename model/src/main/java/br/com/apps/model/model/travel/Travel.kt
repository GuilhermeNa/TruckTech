package br.com.apps.model.model.travel

import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.invalid.InvalidDateException
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
 * @property initialOdometer Odometer reading at the start of the travel.
 * @property finalOdometer Optional odometer reading at the end of the travel. If not provided, it is not yet concluded.
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
    val initialOdometer: BigDecimal,
    val finalOdometer: BigDecimal? = null,
    val isClosed: Boolean,
    val isFinished: Boolean
) : ModelObjectInterface<TravelDto> {

    private val _freights: MutableList<Freight> = mutableListOf()
    val freights: List<Freight> get() = _freights.sortedBy { it.loadingDate }

    private val _refuels: MutableList<Refuel> = mutableListOf()
    val refuels: List<Refuel> get() = _refuels.sortedBy { it.odometerMeasure }

    private val _outlays: MutableList<Outlay> = mutableListOf()
    val outlays: List<Outlay> get() = _outlays.sortedBy { it.date }

    private val _aids: MutableList<TravelAid> = mutableListOf()
    val aids: List<TravelAid> get() = _aids.sortedBy { it.date }

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

        fun List<Travel>.getProfitPercent(): BigDecimal {
            val total = this.sumOf { it.getProfitPercent() }
            return total.divide(BigDecimal(this.size), 2, RoundingMode.HALF_EVEN)
        }

    }

    fun addFreight(freight: Freight) {
        val existingIds = _freights.asSequence().map { it.id }.toSet()

        if (freight.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        if (freight.loadingDate < initialDate) throw InvalidDateException("It is not possible to enter a freight with a date prior to the start of the trip")
        finalDate?.let {
            if (freight.loadingDate > it) throw InvalidDateException("It is not possible to enter a freight with a date after to the end of the trip")
        }

        _freights.add(freight)
    }

    fun addRefuel(refuel: Refuel) {
        val existingIds = _refuels.asSequence().map { it.id }.toSet()

        if (refuel.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        if (refuel.date < initialDate) throw InvalidDateException("It is not possible to enter a refuel with a date prior to the start of the trip")
        finalDate?.let {
            if (refuel.date > it) throw InvalidDateException("It is not possible to enter a refuel with a date after to the end of the trip")
        }

        _refuels.add(refuel)
    }

    fun addOutlay(outlay: Outlay) {
        val existingIds = _outlays.asSequence().map { it.id }.toSet()

        if (outlay.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        if (outlay.date < initialDate) throw InvalidDateException("It is not possible to enter a outlay with a date prior to the start of the trip")
        finalDate?.let {
            if (outlay.date > it) throw InvalidDateException("It is not possible to enter a outlay with a date after to the end of the trip")
        }

        _outlays.add(outlay)
    }

    fun addAid(aid: TravelAid) {
        val existingIds = _aids.asSequence().map { it.id }.toSet()

        if (aid.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)
        if (aid.date < initialDate) throw InvalidDateException("It is not possible to enter a aid with a date prior to the start of the trip")
        finalDate?.let {
            if (aid.date > it) throw InvalidDateException("It is not possible to enter a aid with a date after to the end of the trip")
        }

        _aids.add(aid)
    }

    /**
     * Clear the previous list of freights if is already populated and add new freights.
     */
    fun addAllFreights(freights: List<Freight>) {
        _freights.clear()
        val items = freights.distinctBy { it.id }
        _freights.addAll(items)
    }

    /**
     * Clear the previous list of refuels if is already populated and add new refuels.
     */
    fun addAllRefuels(refuels: List<Refuel>) {
        _refuels.clear()
        val items = refuels.distinctBy { it.id }
        _refuels.addAll(items)
    }

    /**
     * Clear the previous list of outlays if is already populated and add new outlays.
     */
    fun addAllOutlays(outlays: List<Outlay>) {
        _outlays.clear()
        val items = outlays.distinctBy { it.id }
        _outlays.addAll(items)
    }

    /**
     * Clear the previous list of aids if is already populated and add new aids.
     */
    fun addAllAids(aids: List<TravelAid>) {
        _aids.clear()
        val items = aids.distinctBy { it.id }
        _aids.addAll(items)
    }

    /**
     * Retrieves the size of the specified list associated with the travel.
     *
     * @param listTag The tag indicating the type of list (FREIGHT, EXPEND, REFUEL, AID).
     * @return The size of the specified list.
     * @throws InvalidParameterException If an invalid list tag is provided.
     */
    fun getSizeOf(listTag: Int): Int = when (listTag) {
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
    fun getIdsOf(listTag: Int): List<String> = when (listTag) {
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
    fun getCommission(): BigDecimal {
        return _freights.sumOf { it.getCommissionValue() }
    }

    /**
     * Calculates and returns the difference between the initial and final odometer measurements.
     *
     * @return The difference between the initial and final odometer measurements as a BigDecimal.
     */
    fun getTraveledDistance(): BigDecimal =
        finalOdometer?.subtract(initialOdometer) ?: BigDecimal.ZERO

    /**
     * Calculates and returns the total value for the specified list type associated with the travel.
     *
     * @param listTag The tag indicating the type of list (FREIGHT, EXPEND, REFUEL, AID).
     * @return The total value for the specified list type as a BigDecimal.
     * @throws InvalidParameterException If an invalid list tag is provided.
     */
    fun getValueOf(listTag: Int): BigDecimal {
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
        val freight = getValueOf(FREIGHT)
        val commission = _freights.sumOf { it.getCommissionValue() }
        val refuel = getValueOf(REFUEL)
        val expend = getValueOf(OUTLAY)
        return freight.subtract(refuel).subtract(expend).subtract(commission)
            .setScale(2, RoundingMode.HALF_EVEN)
    }

    /**
     * Calculates and returns the travel authentication percentage based on authenticated items.
     *
     * @return The travel authentication percentage as a Double.
     */
    fun getAuthPercent(): Double {
        val authenticableItems =
            getSizeOf(FREIGHT) + getSizeOf(OUTLAY) + getSizeOf(REFUEL) + getSizeOf(AID)
        var authenticated = 0.0

        _freights.forEach { if (it.isValid) authenticated++ }
        _refuels.forEach { if (it.isValid) authenticated++ }
        _outlays.forEach { if (it.isValid) authenticated++ }

        return authenticated.div(authenticableItems.toDouble()) * 100
    }

    /**
     * Checks if the travel is empty (no freights, expenses, refuels, or aids).
     *
     * @return True if the travel is empty, false otherwise.
     */
    fun isEmpty(): Boolean {
        return getSizeOf(FREIGHT) + getSizeOf(OUTLAY) + getSizeOf(REFUEL) + getSizeOf(AID) == 0
    }

    /**
     * Checks if the travel is ready to be finished. It happen when the travel is 100 validated.
     *
     * @return True if the travel is ready to be finished, false otherwise.
     */
    fun isReadyToFinalize(): Boolean {
        _freights.run {
            if (isEmpty()) return false
            else if (any { !it.isValid }) return false
        }
        _outlays.run {
            if(isEmpty()) return false
            else if (any { !it.isValid }) return false
        }
        _refuels.run {
            if (isEmpty()) return false
            else if (any { !it.isValid }) return false
        }
        _aids.run {
            if (any { !it.isValid }) return false
        }
        return true
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
            val value = getValueOf(REFUEL) +
                    getValueOf(OUTLAY) +
                    getCommission()
            return value.setScale(2)
        }

        val profit = getValueOf(FREIGHT)
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
            val distance = getTraveledDistance()
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

    override fun toDto() = TravelDto(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        employeeId = this.employeeId,
        initialDate = this.initialDate.toDate(),
        finalDate = this.finalDate?.toDate(),
        initialOdometer = this.initialOdometer.toDouble(),
        finalOdometer = this.finalOdometer?.toDouble(),
        isFinished = this.isFinished,
        isClosed = this.isClosed
    )

}
