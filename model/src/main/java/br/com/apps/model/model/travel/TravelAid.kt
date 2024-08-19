package br.com.apps.model.model.travel

import br.com.apps.model.dto.travel.TravelAidDto
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.finance.receivable.EmployeeReceivable
import br.com.apps.model.util.toDate
import java.lang.invoke.WrongMethodTypeException
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * The advance payment is made to cover potential expenses incurred by the employee during the course of the trip.
 *
 *  Notes:
 *  * Travel aids are associated with a specific [Employee], [Truck] and [Travel].
 *  * This object needs to be validated ([isValid]) to confirm that the advance has been properly processed.
 *    After processing, it cannot be modified by users without appropriate permissions.
 *
 * @property masterUid Unique identifier for the master record associated with this travel advance.
 * @property id Unique identifier for the [TravelAid].
 * @property employeeId Identifier for the [Employee] receiving the travel advance.
 * @property travelId Identifier for the [Travel] associated with the travel advance.
 * @property date Date and time when the travel aid was issued.
 * @property value Monetary value of the travel aid.
 */
data class TravelAid(
    val masterUid: String,
    val id: String,
    val employeeId: String,
    val travelId: String,
    val date: LocalDateTime,
    val value: BigDecimal,
    val isValid: Boolean,
) : ModelObjectInterface<TravelAidDto> {

    private var _receivable: EmployeeReceivable? = null
    val receivable get() = _receivable

    companion object {
        /**
         * Extension function for list of [TravelAid]'s to merge with a list of [EmployeeReceivable]'s.
         *
         * Each aid in the list will have its receivable updated with
         * the corresponding from the list.
         *
         * @param receivables A list of receivable objects.
         */
        fun List<TravelAid>.merge(receivables: List<EmployeeReceivable>) {
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
     * Sets the receivable for the current aid.
     *
     * @param receivable The `EmployeeReceivable` object to be assigned to the current advance.
     *
     * @throws InvalidIdException If the `parentId` of the provided `EmployeeReceivable` does not match the `id` of the current aid.
     */
    fun setReceivable(receivable: EmployeeReceivable) {
        if (receivable.parentId != id) {
            throw InvalidIdException("Wrong receivable id (${receivable.parentId}) for aid id ($id)")

        } else if (receivable.type != EmployeeReceivableTicket.TRAVEL_AID) {
            throw WrongMethodTypeException("Wrong type: expecting (${EmployeeReceivableTicket.TRAVEL_AID.name}) and received (${receivable.type.name})")

        } else {
            _receivable = receivable

        }
    }

    override fun toDto() = TravelAidDto(
        masterUid = masterUid,
        id = id,
        travelId = travelId,
        employeeId = employeeId,
        date = date.toDate(),
        value = value.toDouble(),
        isValid = isValid
    )

}