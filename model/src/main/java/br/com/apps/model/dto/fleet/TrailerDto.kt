package br.com.apps.model.dto.fleet

import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
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
    override var masterUid: String? = null,
    override var id: String? = null,
    override var plate: String? = null,
    override var type: String? = null,

    var truckId: String? = null

): FleetDto(masterUid = masterUid, id = id, plate = plate, type = type){

    override fun validateDataIntegrity() {
        if (id == null ||
            plate == null ||
            type == null ||
            masterUid == null
        ) throw CorruptedFileException("TrailerDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (plate == null ||
            type == null ||
            masterUid == null
        ) throw InvalidForSavingException("TrailerDto data is invalid: ($this)")
    }

    override fun toModel(): Trailer {
        validateDataIntegrity()
        return Trailer(
            masterUid = masterUid!!,
            id = id!!,
            plate = plate!!,
            type = FleetCategory.valueOf(type!!),
            truckId = truckId
        )
    }

}