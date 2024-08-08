package br.com.apps.model.dto.finance

import br.com.apps.model.enums.TransactionType
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.finance.Transaction
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [Transaction].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class TransactionDto(
    val masterUid: String? = null,
    val id: String? = null,
    val parentId: String? = null,
    val dueDate: Date? = null,
    val number: Int? = null,
    val value: Double? = null,
    val type: String? = null,
    @field:JvmField val isPaid: Boolean? = null
) : DtoObjectInterface<Transaction> {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            parentId == null ||
            dueDate == null ||
            number == null ||
            value == null ||
            type == null ||
            isPaid == null
        ) throw CorruptedFileException("TransactionDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            parentId == null ||
            dueDate == null ||
            number == null ||
            value == null ||
            type == null ||
            isPaid == null
        ) throw InvalidForSavingException("TransactionDto data is invalid: ($this)")
    }

    override fun toModel(): Transaction {
        validateDataIntegrity()
        return Transaction(
            masterUid = masterUid!!,
            id = id!!,
            parentId = parentId!!,
            dueDate = dueDate!!.toLocalDateTime(),
            number = number!!,
            value = BigDecimal(value!!).setScale(2),
            type = TransactionType.valueOf(type!!),
            _isPaid = isPaid!!
        )
    }

}