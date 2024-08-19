package br.com.apps.model.dto.bank

import br.com.apps.model.enums.PixType.Companion.toPixType
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.util.toLocalDateTime
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

) : DtoObjectInterface<BankAccount> {

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

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            employeeId == null ||
            bankId == null ||
            insertionDate == null ||
            branch == null ||
            accNumber == null ||
            mainAccount == null
        ) throw InvalidForSavingException("BankAccountDto data is invalid: ($this)")
    }

    override fun toModel(): BankAccount {
        validateDataIntegrity()
        return BankAccount(
            masterUid = masterUid!!,
            id = id!!,
            employeeId = employeeId!!,
            bankId = bankId!!,
            insertionDate = insertionDate!!.toLocalDateTime(),
            branch = branch!!,
            accNumber = accNumber!!,
            mainAccount = mainAccount!!,
            pix = pix,
            pixType = pixType?.toPixType()
        )
    }

}