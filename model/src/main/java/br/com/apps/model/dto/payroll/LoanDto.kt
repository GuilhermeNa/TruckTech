package br.com.apps.model.dto.payroll

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException
import java.util.Date

data class LoanDto(
    var masterUid: String? = null,
    var id: String? = null,
    var employeeId: String? = null,
    var date: Date? = null,
    var value: Double? = null,
    var installments: Int? = null,
    var installmentsAlreadyPaid: Int? = null,
    @field:JvmField
    var isPaid: Boolean? = null
) : DtoInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            employeeId == null ||
            date == null ||
            installments == null ||
            installmentsAlreadyPaid == null ||
            value == null ||
            isPaid == null
        ) throw CorruptedFileException("LoanDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {}

}