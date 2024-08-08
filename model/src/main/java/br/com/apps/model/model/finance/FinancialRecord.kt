package br.com.apps.model.model.finance

import br.com.apps.model.dto.finance.FinancialRecordDto
import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.model.exceptions.FinancialRecordException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.DUPLICATED_ID
import java.math.BigDecimal
import java.time.LocalDateTime

private const val EMPTY_LIST = "Transaction list is empty"
private const val TRANSACTION_UNPAID =
    "There is at least one Transactions unpaid and this record cannot be set as paid/ received"
private const val DUPLICATED_INSTALLMENT_NUMBER =
    "There is already an item with this installment number"

/**
 * Represents a generic financial record for all payment and receipt events within the system.
 *
 * This abstract class serves as a foundational base for various types of financial records. It is designed
 * to capture and manage financial transactions that arise from entities or events requiring payments or receipts.
 * Examples include maintenance expenses, payroll, freight receivables, and similar financial activities.
 *
 * @property masterUid Unique identifier for the master record associated with this financial entry.
 * @property id Unique identifier for this specific [FinancialRecord].
 * @property parentId Identifier for the entity or event that triggered this financial record
 * (e.g., invoice or contract). Used to link the record to the source of the payment or receivable.
 * @property value Monetary value associated with this record.
 * @property generationDate Date and time when this record was created or generated.
 * @property installments Number of installments for this record, if applicable.
 * @property _transactions List of [Transaction]s associated with this record.
 */
abstract class FinancialRecord(
    open val masterUid: String,
    open val id: String,
    open val parentId: String,
    open val value: BigDecimal,
    open val generationDate: LocalDateTime,
    open val installments: Int,
) : ModelObjectInterface<FinancialRecordDto> {

    private val _transactions: MutableList<Transaction> = mutableListOf()

    fun addTransaction(item: Transaction) {
        val existingIds = _transactions.asSequence().map { it.id }.toSet()
        val existingInstallment = _transactions.asSequence().map { it.number }.toSet()

        if (item.id in existingIds)
            throw DuplicatedItemsException(DUPLICATED_ID)

        if (item.number in existingInstallment)
            throw DuplicatedItemsException(DUPLICATED_INSTALLMENT_NUMBER)

        _transactions.add(item)
    }

    /**
     * Adds a list of transactions to the existing collection of transactions.
     *
     * **Notes:**
     *   - **If ID Does Not Exist**: If the transaction ID is not already present in the existing transactions, the transaction is added to the existing collection.
     *   - **If ID Exists**: If the transaction ID already exists in the list, the transaction is updated with the new transaction data.
     *
     * @param transactions A list of transactions to be added or updated in the existing collection.
     *
     * @throws DuplicatedItemsException If there is an attempt to add a transaction with a duplicate installment number.
     */
    fun addAllTransactions(transactions: List<Transaction>) {
        val existingIds = _transactions.asSequence().map { it.id }.toSet()
        val newItems = transactions.distinctBy { it.id }

        newItems.forEach { item ->
            when (existingIds.contains(item.id)) {
                false -> _transactions.add(item)

                true -> {
                    val itemToReplace = _transactions.first { it.id == item.id }
                    val pos = _transactions.indexOf(itemToReplace)
                    _transactions[pos] = item
                }
            }
        }

    }

    fun clearTransactions() = _transactions.clear()

    fun removeItem(id: String) {
        if (id.isBlank()) throw EmptyIdException("Id cannot be blank")
        _transactions.removeIf { it.id == id }
    }

    fun geTransactions() = _transactions.sortedBy { it.number }

    fun contains(item: Transaction) = _transactions.contains(item)

    fun transactionsSize() = _transactions.size

    fun validateTransactionsPayment() {
        if (_transactions.isEmpty()) throw EmptyDataException(EMPTY_LIST)
        _transactions.forEach {
            if (!it.isPaid) throw FinancialRecordException(TRANSACTION_UNPAID)
        }
    }

    abstract override fun toDto(): FinancialRecordDto

}