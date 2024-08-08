package br.com.apps.model.model.payroll

import br.com.apps.model.dto.payroll.LoanDto
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.exceptions.InvalidIdException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.finance.receivable.EmployeeReceivable
import br.com.apps.model.util.toDate
import java.lang.invoke.WrongMethodTypeException
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This class represents a loan issued to an employee.
 *
 * @property masterUid Unique identifier for the master record associated with this loan.
 * @property id Unique identifier for the [Loan]. This ID is used for tracking and referencing the loan.
 * @property employeeId Identifier for the [Employee] who received the loan. This links the loan to the specific employee.
 * @property date Date and time when the loan was issued. This property captures the exact moment the loan transaction occurred.
 * @property value Monetary amount of the loan. This value represents the total amount of money provided to the employee.
 *
 * @constructor Creates a new loan record with the specified details.
 */
data class Loan(
    val masterUid: String,
    val id: String,
    val employeeId: String,
    val date: LocalDateTime,
    val value: BigDecimal

) : ModelObjectInterface<LoanDto> {

    private var _receivable: EmployeeReceivable? = null
    val receivable get() = _receivable

    companion object {
        /**
         * Extension function for list of [Loan]'s to merge with a list of [EmployeeReceivable]'s.
         *
         * Each loan in the list will have its receivable updated with
         * the corresponding from the list.
         *
         * @param receivables A list of receivable objects.
         */
        fun List<Loan>.merge(receivables: List<EmployeeReceivable>) {
            forEach {
                it.setReceivableById(receivables)
            }
        }
    }

    private fun setReceivableById(receivables: List<EmployeeReceivable>) {
        receivables.firstOrNull {
            it.parentId == id
        }?.let {
            setReceivable(it)
        }
    }

    /**
     * Sets the receivable for the current loan.
     *
     * @param receivable The `EmployeeReceivable` object to be assigned to the current loan.
     *
     * @throws InvalidIdException If the `parentId` of the provided `EmployeeReceivable` does not match the `id` of the current loan.
     */
    fun setReceivable(receivable: EmployeeReceivable) {
        if (receivable.parentId != id) {
            throw InvalidIdException("Wrong receivable id (${receivable.parentId}) for loan id ($id)")

        } else if (receivable.type != EmployeeReceivableTicket.LOAN) {
            throw WrongMethodTypeException("Wrong type: expecting (${EmployeeReceivableTicket.LOAN.name}) and received (${receivable.type.name})")

        } else {
            _receivable = receivable

        }
    }


    override fun toDto() = LoanDto(
        masterUid = masterUid,
        id = id,
        employeeId = employeeId,
        date = date.toDate(),
        value = value.toDouble(),
    )

}
