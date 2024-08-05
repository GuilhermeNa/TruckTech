package br.com.apps.model.dto.payroll

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
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
) : DtoObjectInterface<Loan> {

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

    override fun validateDataForDbInsertion() {}

    override fun toModel(): Loan {
        validateDataIntegrity()
        return Loan(
            masterUid = masterUid!!,
            id = id,
            employeeId = employeeId!!,
            date = date?.toLocalDateTime()!!,
            value = this.value?.let { BigDecimal(it) }!!,
            installments = this.installments!!,
            installmentsAlreadyPaid = this.installmentsAlreadyPaid!!,
            isPaid = this.isPaid!!
        )
    }

}