package br.com.apps.model.dto.finance

import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.finance.FinancialRecord
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [FinancialRecord].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
abstract class FinancialRecordDto(
    open val masterUid: String? = null,
    open val id: String? = null,
    open val parentId: String? = null,
    open val value: Double? = null,
    open val generationDate: Date? = null,
    open val installments: Int? = null,
): DtoObjectInterface<FinancialRecord> {

    abstract override fun toModel(): FinancialRecord

    abstract override fun validateDataForDbInsertion()

    abstract override fun validateDataIntegrity()

}