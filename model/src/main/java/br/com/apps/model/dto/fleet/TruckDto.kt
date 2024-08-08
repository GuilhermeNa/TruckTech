package br.com.apps.model.dto.fleet

import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.exceptions.CorruptedFileException
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
    override var masterUid: String? = null,
    override var id: String? = null,
    override var plate: String? = null,
    override var type: String? = null,

    var employeeId: String? = null,
    var averageAim: Double? = null,
    var performanceAim: Double? = null,
    var commissionPercentual: Double? = null,
    var color: String? = null

): FleetDto(masterUid = masterUid, id = id, plate = plate, type = type){

    override fun validateDataIntegrity() {
        if (id == null ||
            plate == null ||
            color == null ||
            employeeId == null ||
            masterUid == null ||
            type == null ||
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
            employeeId = employeeId!!,
            masterUid = masterUid!!,
            averageAim = averageAim!!,
            performanceAim = performanceAim!!,
            plate = plate!!,
            color = color!!,
            commissionPercentual = BigDecimal(commissionPercentual!!),
            type = FleetCategory.valueOf(type!!)
        )
    }

}