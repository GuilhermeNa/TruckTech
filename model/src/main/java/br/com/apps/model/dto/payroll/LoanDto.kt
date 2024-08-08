package br.com.apps.model.dto.payroll

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [Loan].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class LoanDto(
    var masterUid: String? = null,
    var id: String? = null,
    var employeeId: String? = null,
    var date: Date? = null,
    var value: Double? = null,
) : DtoObjectInterface<Loan> {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            employeeId == null ||
            date == null ||
            value == null
        ) throw CorruptedFileException("LoanDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            employeeId == null ||
            date == null ||
            value == null
        ) throw InvalidForSavingException("LoanDto data is invalid: ($this)")
    }

    override fun toModel(): Loan {
        validateDataIntegrity()
        return Loan(
            masterUid = masterUid!!,
            id = id!!,
            employeeId = employeeId!!,
            date = date?.toLocalDateTime()!!,
            value = BigDecimal(value!!).setScale(2)
        )
    }

}