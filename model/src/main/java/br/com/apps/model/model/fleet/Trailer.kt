package br.com.apps.model.model.fleet

import br.com.apps.model.dto.fleet.TrailerDto
import br.com.apps.model.enums.FleetCategory

/**
 * This class encapsulates details of a trailer, including its identifying information and association with a truck.
 *
 * A trailer can be attached to a truck and is used to transport cargo. It may be assigned to a specific truck during its operation.
 *
 * @property masterUid Unique identifier for the master record associated with this trailer.
 * @property id Unique identifier for the [Trailer]. This ID must be provided for identification purposes.
 * @property plate License plate number of the trailer.
 * @property type Category of the fleet to which the trailer belongs. Represents the size or type of the fleet.
 * @property truckId Optional identifier for the [Truck] to which this trailer is currently assigned. If null, the trailer is not assigned to any truck.
 *
 * @constructor Creates a new trailer record with the specified details.
 */
data class Trailer(
    override val masterUid: String,
    override var id: String,
    override val plate: String,
    override val type: FleetCategory,

    val truckId: String? = null

) : Fleet(masterUid = masterUid, id = id, plate = plate, type = type) {

    override fun toDto() = TrailerDto(
        masterUid = masterUid,
        id = id,
        plate = plate,
        type = type.name,
        truckId = truckId
    )


}