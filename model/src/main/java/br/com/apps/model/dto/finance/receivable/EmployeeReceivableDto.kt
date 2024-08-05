package br.com.apps.model.dto.finance.receivable

import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.finance.receivable.EmployeeReceivable
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [EmployeeReceivable].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class EmployeeReceivableDto(
    override val masterUid: String? = null,
    override val id : String? = null,
    override val parentId: String? = null,
    override val value: Double? = null,
    override val generationDate: Date? = null,
    override val installments: Int? = null,

    @field:JvmField
    val isReceived: Boolean? = null,
    val employeeId: String? = null,
    val type: String? = null

): ReceivableDto(
    masterUid = masterUid, id = id, parentId = parentId, value = value,
    generationDate = generationDate, installments = installments
) {

    override fun validateDataIntegrity() {
        if(masterUid  == null||
            id  == null||
            parentId  == null||
            installments  == null||
            value  == null||
            type  == null||
            generationDate  == null||
            isReceived  == null||
            employeeId == null
        ) throw CorruptedFileException("EmployeeReceivableDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if(masterUid  == null||
            parentId  == null||
            installments  == null||
            value  == null||
            type  == null||
            generationDate  == null||
            isReceived  == null||
            employeeId == null
        ) throw InvalidForSavingException("EmployeePayableDto data is invalid: ($this)")
    }

    override fun toModel(): EmployeeReceivable {
        validateDataIntegrity()
        return EmployeeReceivable(
            masterUid = masterUid!!,
            id = id!!,
            parentId = parentId!!,
            installments = installments!!,
            value = BigDecimal(value!!).setScale(2),
            type = EmployeeReceivableTicket.valueOf(type!!),
            generationDate = generationDate!!.toLocalDateTime(),
            _isReceived = isReceived!!,
            employeeId = employeeId!!
        )
    }

}