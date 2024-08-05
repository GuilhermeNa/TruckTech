package br.com.apps.model.dto.fleet

import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.fleet.Truck
import java.math.BigDecimal

/**
 * Data Transfer Object (DTO) representing a [Truck].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class TruckDto(
    var id: String? = null,
    var masterUid: String? = null,
    var driverId: String? = null,

    var averageAim: Double? = null,
    var performanceAim: Double? = null,
    var plate: String? = null,
    var color: String? = null,
    var commissionPercentual: Double? = null,
    var fleetType: String? = null
) : DtoObjectInterface<Truck> {

    override fun validateDataIntegrity() {
        if (id == null ||
            plate == null ||
            color == null ||
            driverId == null ||
            masterUid == null ||
            fleetType == null ||
            averageAim == null ||
            performanceAim == null ||
            commissionPercentual == null
        ) throw CorruptedFileException("TruckDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {}

    override fun toModel(): Truck {
        validateDataIntegrity()
        return Truck(
            id = id!!,
            employeeId = driverId!!,
            masterUid = masterUid!!,
            averageAim = averageAim!!,
            performanceAim = performanceAim!!,
            plate = plate ?: "-",
            color = color ?: "-",
            commissionPercentual = BigDecimal(commissionPercentual!!),
            fleetType = FleetCategory.valueOf(fleetType!!)
        )
    }

}