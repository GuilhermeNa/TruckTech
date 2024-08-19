package br.com.apps.model.model.payroll

import br.com.apps.model.dto.payroll.AdvanceDto
import br.com.apps.model.enums.AdvanceType
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.finance.receivable.EmployeeReceivable
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.util.toDate
import java.lang.invoke.WrongMethodTypeException
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This class represents an advance payment associated with an employee.
 *
 * @property masterUid Unique identifier for the master record associated with this advance.
 * @property id Unique identifier for the [Advance]. This ID is used for tracking and reference purposes.
 * @property travelId Optional identifier for the [Travel] associated with this advance. Can be null if the advance is not linked to a specific travel.
 * @property employeeId Identifier for the [Employee] who received the advance. This ID links the advance to the respective employee.
 * @property date Date and time when the advance was issued. This property captures the exact moment of the transaction.
 * @property value Monetary amount of the advance. This value represents the amount of money provided to the employee.
 * @property type The type of advance, represented by an [AdvanceType]. This categorizes the advance and defines its nature.
 */
data class Advance(
    val masterUid: String,
    val id: String,
    val travelId: String? = null,
    val employeeId: String,
    val date: LocalDateTime,
    val value: BigDecimal,
    val type: AdvanceType
): ModelObjectInterface<AdvanceDto> {

    private var _receivable: EmployeeReceivable? = null
    val receivable get() = _receivable

    companion object {
        /**
         * Extension function for list of [Advance]'s to merge with a list of [EmployeeReceivable]'s.
         *
         * Each advance in the list will have its receivable updated with
         * the corresponding from the list.
         *
         * @param receivables A list of receivable objects.
         */
        fun List<Advance>.merge(receivables: List<EmployeeReceivable>) {
            forEach {
                it.setReceivableById(receivables)
            }
        }
    }

    private fun setReceivableById(receivables: List<EmployeeReceivable>) {
        receivables.firstOrNull {
            it.parentId == id
        }?.let {
            setReceivable(it)
        }
    }

    /**
     * Sets the receivable for the current advance.
     *
     * @param receivable The `EmployeeReceivable` object to be assigned to the current advance.
     *
     * @throws InvalidIdException If the `parentId` of the provided `EmployeeReceivable` does not match the `id` of the current advance.
     */
    fun setReceivable(receivable: EmployeeReceivable) {
        if (receivable.parentId != id) {
            throw InvalidIdException("Wrong receivable id (${receivable.parentId}) for loan id ($id)")

        } else if (receivable.type != EmployeeReceivableTicket.ADVANCE) {
            throw WrongMethodTypeException("Wrong type: expecting (${EmployeeReceivableTicket.ADVANCE.name}) and received (${receivable.type.name})")

        } else {
            _receivable = receivable

        }
    }

    override fun toDto() = AdvanceDto(
            masterUid = masterUid,
            id = id,
            travelId = travelId,
            employeeId = employeeId,
            date = date.toDate(),
            value = value.toDouble(),
            type = type.toString()
        )

}


