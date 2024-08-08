package br.com.apps.model.model.finance.payable

import br.com.apps.model.dto.finance.payable.PayableDto
import br.com.apps.model.model.finance.FinancialRecord
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Represents a financial record categorized as a payable.
 *
 * Examples:
 * * Payroll, maintenance expenses, refueling costs, and other similar financial obligations.
 *
 * @property _isPaid Indicates whether the payable has been paid (true) or not (false).
 */
abstract class Payable(
    override val masterUid: String,
    override val id: String,
    override val parentId: String,
    override val value: BigDecimal,
    override val generationDate: LocalDateTime,
    override val installments: Int,

    private var _isPaid: Boolean

) : FinancialRecord(
    masterUid = masterUid, id = id, parentId = parentId, value = value,
    generationDate = generationDate, installments = installments
) {

    val isPaid: Boolean
        get() = _isPaid

    fun registerPayment() {
        validateTransactionsPayment()
        _isPaid = true
    }

    abstract override fun toDto(): PayableDto

}