package br.com.apps.model.model.fleet

import br.com.apps.model.dto.fleet.TruckDto
import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.model.employee.Driver
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.util.DUPLICATED_ID
import java.math.BigDecimal

private const val DUPLICATED_PLATE =
    "There is already an item with this plate"

/**
 * This class encapsulates details of a truck, including its identifying information, performance metrics, and associated trailers.
 *
 * Notes:
 * * A truck is used to perform a [Travel]'s.
 * * An employee([Driver]) is required to be assigned to the truck.
 * * The truck can have multiple associated [Trailer]'s that are managed as part of this record.
 *
 * @property masterUid Unique identifier for the master record associated with this truck.
 * @property id Optional unique identifier for the [Truck].
 * @property plate License plate number of the truck.
 * @property type Category of the fleet to which the truck belongs. Represents the size or type of the fleet.
 * @property employeeId Identifier for the [Employee] assigned to this truck.
 * @property averageAim The target average performance metric for the truck, such as fuel efficiency.
 * @property performanceAim The target performance metric for the truck, representing the profit percent goal.
 * @property color The color of the truck, used for identification and aesthetic purposes.
 * @property commissionPercentual The percentage of commission associated with the truck,
 * used to define [Freight]'s comission for drivers.
 * @property _trailers List of [Trailer]s associated with this truck. Represents the trailers that are attached to or used with the truck.
 */
data class Truck(
    override val masterUid: String,
    override val id: String,
    override val plate: String,
    override val type: FleetCategory,

    val employeeId: String,
    val averageAim: Double,
    val performanceAim: Double,
    val color: String,
    val commissionPercentual: BigDecimal,
    private val _trailers: MutableList<Trailer> = mutableListOf()

) : Fleet(masterUid = masterUid, id = id, plate = plate, type = type) {

    val trailers: List<Trailer>
        get() = _trailers

    /**
     * Retrieves a list of all fleet IDs associated with the truck, including its own ID
     * and the IDs of any trailers attached to it.
     *
     * @return List of fleet IDs as [String].
     */
    fun getFleetIds(): List<String> {
        val fleetIds = mutableListOf<String>()
        fleetIds.add(id)
        _trailers.forEach { t -> fleetIds.add(t.id) }
        return fleetIds
    }

    fun addTrailer(trailer: Trailer) {
        val existingIds = _trailers.asSequence().map { it.id }.toSet()
        val existingInstallment = _trailers.asSequence().map { it.plate }.toSet()

        if (trailer.id in existingIds) throw DuplicatedItemsException(DUPLICATED_ID)

        if (trailer.plate in existingInstallment) throw DuplicatedItemsException(DUPLICATED_PLATE)

         _trailers.add(trailer)
    }

    fun clearTrailers() = _trailers.clear()

    fun contains(trailer: Trailer) = _trailers.contains(trailer)

    override fun toDto(): TruckDto = TruckDto(
        id = id,
        employeeId = employeeId,
        masterUid = masterUid,
        averageAim = averageAim,
        performanceAim = performanceAim,
        plate = plate,
        color = color,
        commissionPercentual = commissionPercentual.toDouble(),
        type = type.toString()
    )

}