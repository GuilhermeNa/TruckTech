package br.com.apps.model.model.employee

import br.com.apps.model.dto.employee_dto.AssistantDto
import br.com.apps.model.enums.WorkRole

/**
 * This class represents an employee who assists in administrative tasks.
 *
 * Notes:
 * * An assistant is a specific type of [Employee] with responsibilities related to administrative tasks.
 *
 * @property masterUid Unique identifier for the master record associated with this assistant.
 * @property id Unique identifier for the [Assistant].
 * @property name Name of the assistant.
 * @property type The WorkRole this assistant performs.
 */
data class Assistant(
    override val masterUid: String,
    override val id: String,
    override val name: String,
    override val type: WorkRole

) : Employee(masterUid = masterUid, id = id, name = name, type = type) {

    override fun toDto() = AssistantDto(
        masterUid = masterUid,
        id = id,
        name = name,
        type = type.name
    )

}