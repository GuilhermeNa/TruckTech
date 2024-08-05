package br.com.apps.model.model.travel

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This class encapsulates details of a refueling event for a truck.
 *
 *  Notes:
 *  * Refuel events are associated with a specific [Truck] and optionally a [Travel].
 *  * This object needs to be validated([isValid]) by an administrator to verify its accuracy.
 *    After validation, it cannot be modified by users without appropriate permissions.
 *
 * @property masterUid Unique identifier for the master record associated with this refuel event.
 * @property id Unique identifier for the [Refuel].
 * @property truckId Identifier for the [Truck] being refueled.
 * @property travelId Optional identifier for the [Travel] associated with this refuel, if applicable.
 * @property date Date and time when the refuel event occurred.
 * @property station Name or identifier of the refueling station where the fuel was purchased.
 * @property odometerMeasure Odometer reading at the time of the refuel.
 * @property valuePerLiter Price of fuel per liter at the time of refueling.
 * @property amountLiters Quantity of fuel added to the truck, measured in liters.
 * @property totalValue Total monetary value of the fuel purchased.
 * @property isCompleteRefuel Indicates whether the tank was filled to its full capacity during the refuel (true) or not (false).
 * @property isValid Indicates whether the refuel record is valid (true) or not (false). This property
 * must only be set by an Admin user.
 */
data class Refuel(
    val masterUid: String,
    var id: String,
    val truckId: String,
    val travelId: String? = null,
    var date: LocalDateTime,
    var station: String,
    var odometerMeasure: BigDecimal,
    var valuePerLiter: BigDecimal,
    var amountLiters: BigDecimal,
    var totalValue: BigDecimal,
    @field:JvmField var isCompleteRefuel: Boolean,
    @field:JvmField var isValid: Boolean
) : ModelObjectInterface<RefuelDto> {

    override fun toDto() = RefuelDto(
        masterUid = masterUid,
        id = id,
        truckId = truckId,
        travelId = travelId,
        date = date.toDate(),
        station = station,
        odometerMeasure = odometerMeasure.toDouble(),
        valuePerLiter = valuePerLiter.toDouble(),
        amountLiters = amountLiters.toDouble(),
        totalValue = totalValue.toDouble(),
        isCompleteRefuel = isCompleteRefuel,
        isValid = isValid
    )

}