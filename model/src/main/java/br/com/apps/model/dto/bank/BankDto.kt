package br.com.apps.model.dto.bank

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.bank.Bank

/**
 * Data Transfer Object (DTO) representing a [Bank].
 *
 * This class is used to transfer [Bank] information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
class BankDto(
    var id: String? = null,
    var name: String? = null,
    var code: Int? = null,
    var urlImage: String? = null
) : DtoObjectInterface<Bank> {

    override fun validateDataIntegrity() {
        if ((id == null || name == null || code == null || urlImage == null))
            throw CorruptedFileException("BankDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {}

    override fun toModel(): Bank {
        validateDataIntegrity()
        return Bank(
            id = this.id!!,
            name = this.name!!,
            code = this.code!!.toInt(),
            urlImage = this.urlImage!!
        )
    }

}