package br.com.apps.model.dto.bank

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException

class BankDto(
    var id: String? = null,
    var name: String? = null,
    var code: Int? = null,
    var urlImage: String? = null
) : DtoInterface {

    override fun validateDataIntegrity() {
        if((id == null || name == null || code == null || urlImage == null))
            throw CorruptedFileException("BankDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {}

}

