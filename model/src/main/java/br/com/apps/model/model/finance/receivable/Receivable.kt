package br.com.apps.model.model.finance.receivable

import br.com.apps.model.dto.finance.receivable.ReceivableDto
import br.com.apps.model.model.finance.FinancialRecord
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Represents a financial record that is categorized as a receivable.
 *
 * Examples:
 * * Customer payments, reimbursement claims, and other similar financial receivables.
 *
 * @property _isReceived Indicates whether the receivable has been received (true) or not (false).
 */
abstract class Receivable(
    override val masterUid: String,
    override val id: String,
    override val parentId: String,
    override val value: BigDecimal,
    override val generationDate: LocalDateTime,
    override val installments: Int,

    private var _isReceived: Boolean

) : FinancialRecord(
    masterUid = masterUid, id = id, parentId = parentId, value = value,
    generationDate = generationDate, installments = installments
) {

    val isReceived: Boolean
        get() = _isReceived

    fun registerReceivement() {
        validateTransactionsPayment()
        _isReceived = true
    }

    abstract override fun toDto(): ReceivableDto

}