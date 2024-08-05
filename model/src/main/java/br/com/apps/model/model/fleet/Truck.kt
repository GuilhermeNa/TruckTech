package br.com.apps.model.model.fleet

import br.com.apps.model.dto.fleet.TruckDto
import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.employee.Driver
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import java.math.BigDecimal

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
 * @property fleetType Category of the fleet to which the truck belongs. Represents the size or type of the fleet.
 * @property employeeId Identifier for the [Employee] assigned to this truck.
 * @property averageAim The target average performance metric for the truck, such as fuel efficiency.
 * @property performanceAim The target performance metric for the truck, representing the profit percent goal.
 * @property color The color of the truck, used for identification and aesthetic purposes.
 * @property commissionPercentual The percentage of commission associated with the truck,
 * used to define [Freight]'s comission for drivers.
 * @property trailers List of [Trailer]s associated with this truck. Represents the trailers that are attached to or used with the truck.
 */
data class Truck(
    override val masterUid: String,
    override val id: String,
    override val plate: String,
    override val fleetType: FleetCategory,
    val employeeId: String,
    val averageAim: Double,
    val performanceAim: Double,
    val color: String,
    val commissionPercentual: BigDecimal,
    val trailers: MutableList<Trailer> = mutableListOf()
) : Fleet(
    masterUid = masterUid,
    id = id,
    plate = plate,
    fleetType = fleetType
), ModelObjectInterface<TruckDto> {

    override fun toDto(): TruckDto = TruckDto(
        id = id,
        driverId = employeeId,
        masterUid = masterUid,
        averageAim = averageAim,
        performanceAim = performanceAim,
        plate = plate,
        color = color,
        commissionPercentual = commissionPercentual.toDouble(),
        fleetType = fleetType.toString()
    )

    /**
     * Retrieves a list of all fleet IDs associated with the truck, including its own ID
     * and the IDs of any trailers attached to it.
     *
     * @return List of fleet IDs as [String].
     */
    fun getFleetIds(): List<String> {
        val fleetIds = mutableListOf<String>()
        fleetIds.add(id)
        trailers.forEach { t -> fleetIds.add(t.id) }
        return fleetIds
    }

    /**
     * This method appends the specified [trailer] to the internal list of trailers,
     * allowing you to manage and track multiple trailers associated with this truck.
     *
     * @param trailer The [Trailer] object to be added to the list. It cannot be null.
     *
     * @throws IllegalArgumentException if the [trailer] is null.
     */
    fun addTrailer(trailer: Trailer) = trailers.add(trailer)

    /**
     * This method clears the internal list of trailers, effectively removing all
     * trailers associated with this truck. After calling this method, the list will be empty.
     *
     * This method does not return any value and does not throw any exception.
     */
    fun clearTrailers() = trailers.clear()

}