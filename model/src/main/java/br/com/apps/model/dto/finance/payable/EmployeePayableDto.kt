package br.com.apps.model.dto.finance.payable

import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.model.finance.payable.EmployeePayable
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [EmployeePayable].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class EmployeePayableDto(
    override val masterUid: String? = null,
    override val id: String? = null,
    override val parentId: String? = null,
    override val value: Double? = null,
    override val generationDate: Date? = null,
    override val installments: Int? = null,

    @field:JvmField
    val isPaid: Boolean? = null,
    val employeeId: String? = null,
    val type: String? = null

) : PayableDto(
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
            isPaid  == null||
            employeeId == null
        ) throw CorruptedFileException("EmployeePayableDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if(masterUid  == null||
            parentId  == null||
            installments  == null||
            value  == null||
            type  == null||
            generationDate  == null||
            isPaid  == null||
            employeeId == null
        ) throw InvalidForSavingException("EmployeePayableDto data is invalid: ($this)")
    }

    override fun toModel(): EmployeePayable {
        validateDataIntegrity()
        return EmployeePayable(
            masterUid = masterUid!!,
            id = id!!,
            parentId = parentId!!,
            installments = installments!!,
            value = BigDecimal(value!!).setScale(2),
            type = EmployeePayableTicket.valueOf(type!!),
            generationDate = generationDate!!.toLocalDateTime(),
            _isPaid = isPaid!!,
            employeeId = employeeId!!
        )
    }

}