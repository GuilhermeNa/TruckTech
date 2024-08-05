package br.com.apps.model.model.finance.receivable

import br.com.apps.model.dto.finance.receivable.EmployeeReceivableDto
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.finance.Transaction
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Represents a receivable financial record specifically related to an [Employee].
 *
 * Examples:
 * * Reimbursement of loans,
 *   advances, and other similar receivables, etc.
 *
 * @property employeeId Unique identifier for the [Employee] related to this receivable.
 * @property type Type of [EmployeeReceivableTicket].
 * @property _isReceived Indicates whether the receivable has been received (true) or not (false).
 */
data class EmployeeReceivable(
    override val masterUid: String,
    override val id: String,
    override val parentId: String,
    override val value: BigDecimal,
    override val generationDate: LocalDateTime,
    override val installments: Int,
    override val transactions: MutableList<Transaction> = mutableListOf(),

    val employeeId: String,
    val type: EmployeeReceivableTicket,
    private val _isReceived: Boolean

) : Receivable(
    masterUid = masterUid, id = id, parentId = parentId, value = value,
    generationDate = generationDate, transactions = transactions,
    _isReceived = _isReceived, installments = installments
) {

    override fun toDto() = EmployeeReceivableDto(
        masterUid = masterUid,
        id = id,
        employeeId = employeeId,
        parentId = parentId,
        installments = installments,
        value = value.toDouble(),
        type = type.name,
        generationDate = generationDate.toDate(),
        isReceived = isReceived
    )

}