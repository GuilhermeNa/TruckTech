package br.com.apps.model.model.payroll

import br.com.apps.model.dto.payroll.LoanDto
import br.com.apps.model.exceptions.InvalidValueException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

data class Loan(
    val masterUid: String,
    val id: String? = null,
    val employeeId: String,
    val date: LocalDateTime,
    var value: BigDecimal,
    var installments: Int,
    val installmentsAlreadyPaid: Int,
    @field:JvmField
    val isPaid: Boolean
) : ModelObjectInterface<LoanDto> {

    /**
     * Calculates the value of each installment for the loan.
     *
     * @return The calculated value of each installment, computed as total value divided by number of installments.
     * @throws InvalidValueException If the number of installments is zero.
     */
    fun getInstallmentValue(): BigDecimal {
        return if (installments == 0) throw InvalidValueException("Number of installments not defined")
        else value.divide(BigDecimal(installments), 2, RoundingMode.HALF_EVEN)
    }

    override fun toDto() = LoanDto(
        masterUid = masterUid,
        id = id,
        employeeId = employeeId,
        date = date.toDate(),
        value = value.toDouble(),
        installments = installments,
        installmentsAlreadyPaid = installmentsAlreadyPaid,
        isPaid = isPaid
    )

}
