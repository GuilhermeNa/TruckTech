package br.com.apps.model.dto.fleet

import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.fleet.Fleet

/**
 * Data Transfer Object (DTO) representing a [Fleet].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
abstract class FleetDto(
    open val masterUid: String? = null,
    open val plate: String? = null,
    open val type: String? = null,
    open val id: String? = null
) : DtoObjectInterface<Fleet> {

    abstract override fun toModel(): Fleet

    abstract override fun validateDataForDbInsertion()

    abstract override fun validateDataIntegrity()

}