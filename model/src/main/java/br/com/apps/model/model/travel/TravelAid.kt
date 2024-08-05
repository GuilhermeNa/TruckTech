package br.com.apps.model.model.travel

import br.com.apps.model.dto.payroll.TravelAidDto
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.util.toDate
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
    var id: String,
    val employeeId: String,
    val travelId: String,
    val date: LocalDateTime,
    var value: BigDecimal,
    @field:JvmField val isValid: Boolean,
) : ModelObjectInterface<TravelAidDto> {

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