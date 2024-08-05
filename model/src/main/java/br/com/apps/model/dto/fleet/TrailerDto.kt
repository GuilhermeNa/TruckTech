package br.com.apps.model.dto.fleet

import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.fleet.Trailer

/**
 * Data Transfer Object (DTO) representing a [Trailer].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class TrailerDto(
    var masterUid: String? = null,
    var id: String? = null,
    var plate: String? = null,
    var fleetType: String? = null,
    var truckId: String? = null,
) : DtoObjectInterface<Trailer> {

    override fun validateDataIntegrity() {
        if (id == null ||
            plate == null ||
            truckId == null ||
            fleetType == null ||
            masterUid == null
        ) throw CorruptedFileException("TrailerDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {

    }

    override fun toModel(): Trailer {
        validateDataIntegrity()
        return Trailer(
            masterUid = masterUid!!,
            id = id!!,
            plate = plate!!,
            fleetType = FleetCategory.valueOf(fleetType!!),
            truckId = truckId
        )
    }

}