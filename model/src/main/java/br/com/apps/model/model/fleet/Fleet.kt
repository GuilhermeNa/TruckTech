package br.com.apps.model.model.fleet

import br.com.apps.model.dto.fleet.FleetDto
import br.com.apps.model.enums.FleetCategory
import br.com.apps.model.interfaces.ModelObjectInterface

/**
 * This abstract class represents a fleet entity, which includes identifying information and categorization.
 *
 * @property masterUid Unique identifier for the master record associated with this fleet entity.
 * @property id Unique identifier for the [Fleet]. This ID is used for identification purposes across the system.
 * @property plate License plate number of the fleet entity. This property is used for vehicle identification.
 * @property type Category of the fleet to which this entity belongs. Represents the size or type of the fleet.
 */
abstract class Fleet(
    open val masterUid: String,
    open val plate: String,
    open val id: String,
    open val type: FleetCategory
): ModelObjectInterface<FleetDto> {

    abstract override fun toDto(): FleetDto

}



