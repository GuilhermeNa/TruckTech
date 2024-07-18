package br.com.apps.model.dto.employee_dto

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import java.util.Date

data class BankAccountDto(
    var masterUid: String? = null,
    var id: String? = null,
    var employeeId: String? = null,
    var insertionDate: Date? = null,
    var bankName: String? = null,
    var branch: Int? = null,
    var accNumber: Int? = null,
    var pix: String? = null,
    var code: String? = null,
    var mainAccount: Boolean? = false,
    var pixType: String? = null
) : DtoInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            employeeId == null ||
            insertionDate == null ||
            code == null ||
            bankName == null ||
            branch == null ||
            accNumber == null ||
            mainAccount == null
        ) throw CorruptedFileException("BankAccountDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {
        if (masterUid == null ||
            employeeId == null ||
            insertionDate == null ||
            code == null ||
            bankName == null ||
            branch == null ||
            accNumber == null ||
            mainAccount == null
        ) throw InvalidForSavingException("BankAccountDto data is invalid: ($this)")
    }

}