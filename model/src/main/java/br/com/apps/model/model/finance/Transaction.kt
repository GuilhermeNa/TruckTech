package br.com.apps.model.model.finance

import br.com.apps.model.dto.finance.TransactionDto
import br.com.apps.model.enums.TransactionType
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This class represents a financial transaction within the system.
 *
 * Notes:
 * * Each transaction is associated with a [FinancialRecord].
 * * Represents an installment of the associated [FinancialRecord].
 *
 * Example:
 * * If a financial record has 3 installments, there will be 3 transactions created for this record,
 *   each corresponding to one installment.
 *
 * @property masterUid Unique identifier for the master record associated with this transaction.
 * @property id Unique identifier for the [Transaction].
 * @property parentKey Identifier for the [FinancialRecord] associated with this transaction (if applicable).
 * @property dueDate Date and time when the transaction is due.
 * @property number number of the installment sequentially.
 * @property value Monetary value of the transaction.
 * @property type Type of the [TransactionType], indicating the category or nature of the transaction.
 * @property _isPaid Private mutable flag indicating whether the transaction has been paid.
 */
data class Transaction(
    val masterUid: String,
    val id: String,
    val parentKey: String,
    val dueDate: LocalDateTime,
    val number: Int,
    val value: BigDecimal,
    val type: TransactionType,
    private var _isPaid: Boolean
): ModelObjectInterface<TransactionDto> {

    val isPaid: Boolean
        get() = _isPaid

    /**
     * Marks the transaction as paid.
     * This method updates the internal state of the transaction to reflect that it has been completed.
     */
    fun processTransaction() { _isPaid = true }

    /**
     * Reverts the transaction to an unpaid state.
     * This method updates the internal state of the transaction to reflect that it is no longer completed.
     */
    fun undoTransaction() { _isPaid = false }

    override fun toDto() = TransactionDto(
        masterUid = masterUid,
        id = id,
        parentId = parentKey,
        dueDate = dueDate.toDate(),
        number = number,
        value = value.toDouble(),
        type = type.name,
        isPaid = isPaid
    )

}