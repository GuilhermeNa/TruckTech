package br.com.apps.model.model.finance.payable

import br.com.apps.model.dto.finance.payable.EmployeePayableDto
import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.finance.Transaction
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Represents a payable financial record specifically related to an [Employee].
 *
 * Examples:
 * * Reimbursement of expenses, commissions, vacation pay,
 *   salary advances, thirteenth-month salary, and other similar receivables, etc.
 *
 * @property employeeId Unique identifier for the [Employee] related to this payable.
 * @property type Type of [EmployeePayableTicket].
 * @property _isPaid Indicates whether the payable has been paid (true) or not (false).
 */
data class EmployeePayable(
    override val masterUid: String,
    override val id: String,
    override val parentId: String,
    override val value: BigDecimal,
    override val generationDate: LocalDateTime,
    override val installments: Int,
    override val transactions: MutableList<Transaction> = mutableListOf(),

    val employeeId: String,
    val type: EmployeePayableTicket,
    private val _isPaid: Boolean

): Payable(
    masterUid = masterUid, id = id, parentId = parentId, value = value,
    generationDate = generationDate, transactions = transactions,
    _isPaid = _isPaid, installments = installments
) {

    override fun toDto() = EmployeePayableDto(
        masterUid = masterUid,
        id = id,
        employeeId = employeeId,
        parentId = parentId,
        value = value.toDouble(),
        type = type.name,
        generationDate = generationDate.toDate(),
        isPaid = isPaid,
        installments = installments,
    )

}