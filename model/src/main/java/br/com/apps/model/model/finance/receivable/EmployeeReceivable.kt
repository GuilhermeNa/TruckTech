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

    val employeeId: String,
    val type: EmployeeReceivableTicket,
    private val _isReceived: Boolean

) : Receivable(
    masterUid = masterUid, id = id, parentId = parentId, value = value,
    generationDate = generationDate, _isReceived = _isReceived, installments = installments
) {

    companion object {

        /**
         * Extension function for list of [EmployeeReceivable]'s to merge with a list of [Transaction]'s.
         *
         * Each receivable in the list will have its transactions updated with
         * the corresponding from the transactions list.
         *
         * @param transactions A list of transaction objects.
         *
         * @return A list of receivables with valid transactions.
         */
        fun List<EmployeeReceivable>.merge(transactions: List<Transaction>) {
            this.forEach { receivable ->
                val items = transactions.filter { it.parentId == receivable.id }
                receivable.addAllTransactions(items)
            }
        }

    }

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