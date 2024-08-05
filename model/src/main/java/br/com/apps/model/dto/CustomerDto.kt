package br.com.apps.model.dto

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.Customer

/**
 * Data Transfer Object (DTO) representing a [Customer].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class CustomerDto(
    val masterUid: String? = null,
    val id: String? = null,
    val cnpj: String? = null,
    val name: String? = null
) : DtoObjectInterface<Customer> {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            cnpj == null ||
            name == null
        ) throw CorruptedFileException("CustomerDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        TODO("Not yet implemented")
    }

    override fun toModel(): Customer {
        validateDataIntegrity()
        return Customer(
            masterUid = masterUid!!,
            id = id!!,
            cnpj = cnpj!!,
            name = name!!
        )
    }

}