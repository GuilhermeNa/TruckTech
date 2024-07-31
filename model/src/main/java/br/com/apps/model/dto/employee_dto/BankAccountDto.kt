package br.com.apps.model.dto.employee_dto

import br.com.apps.model.dto.DtoObjectsInterface
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.bank.BankAccount
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [BankAccount].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class BankAccountDto(
    // Ids
    var masterUid: String? = null,
    var id: String? = null,
    var employeeId: String? = null,
    var bankId: String? = null,

    // Others
    var insertionDate: Date? = null,
    var branch: Int? = null,
    var accNumber: Int? = null,
    var mainAccount: Boolean? = false,
    var pix: String? = null,
    var pixType: String? = null

) : DtoObjectsInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            employeeId == null ||
            bankId == null ||
            insertionDate == null ||
            branch == null ||
            accNumber == null ||
            mainAccount == null
        ) throw CorruptedFileException("BankAccountDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {
        if (masterUid == null ||
            employeeId == null ||
            bankId == null ||
            insertionDate == null ||
            branch == null ||
            accNumber == null ||
            mainAccount == null
        ) throw InvalidForSavingException("BankAccountDto data is invalid: ($this)")
    }

}