package br.com.apps.model.dto.finance.payable

import br.com.apps.model.dto.finance.FinancialRecordDto
import br.com.apps.model.model.finance.payable.Payable
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [Payable].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
abstract class PayableDto(
    override val masterUid: String? = null,
    override val id: String? = null,
    override val parentId: String? = null,
    override val value: Double? = null,
    override val generationDate: Date? = null,
    override val installments: Int? = null,
) : FinancialRecordDto(
    masterUid = masterUid, id = id, parentId = parentId, value = value,
    generationDate = generationDate, installments = installments
) {

    abstract override fun validateDataIntegrity()

    abstract override fun validateDataForDbInsertion()

    abstract override fun toModel(): Payable

}
