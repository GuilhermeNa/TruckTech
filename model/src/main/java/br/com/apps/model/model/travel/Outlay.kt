package br.com.apps.model.model.travel

import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.interfaces.LabelInterface
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.model.label.Label
import br.com.apps.model.util.ERROR_STRING
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This class encapsulates details of an expenditure associated with a truck and an employee due a travel.
 *
 *  Notes:
 *  * Expenditures are related to a specific [Truck], [Employee], and to a [Travel].
 *  * Due the various possibilities for expenditure types, it uses [Label]'s (which can be
 *    created by the admin user) to define its type and additional info.
 *  * This object needs to be validated([isValid]) by an administrator to verify its accuracy.
 *    After validation, it cannot be modified by users without appropriate permissions.
 *  * If an expenditure is paid by the employee([isPaidByEmployee]), a reimbursement debt will be generated and needs to be settled([isAlreadyRefunded]).
 *
 * @property masterUid Unique identifier for the master record associated with this expenditure.
 * @property id Optional unique identifier for the [Outlay].
 * @property truckId Identifier for the [Truck] associated with this expenditure.
 * @property employeeId Identifier for the [Employee] associated with this expenditure.
 * @property travelId Identifier for the [Travel] associated with this expenditure.
 * @property labelId Identifier for the [Label] associated with this expenditure.
 * @property company Name of the company where the expenditure was incurred.
 * @property date Date and time when the expenditure was recorded.
 * @property description Description of the expenditure.
 * @property value Monetary value of the expenditure.
 * @property isPaidByEmployee Indicates whether the expenditure was paid by the employee (true) or not (false).
 * @property isValid Indicates whether the expenditure record is valid (true) or not (false). This property
 * must only be set by an Assistant user.
 * @property label Optional object containing additional details related to the expenditure.
 */
data class Outlay(
    val masterUid: String,
    val id: String,
    val truckId: String,
    val employeeId: String,
    val travelId: String,
    val labelId: String,
    val company: String,
    val date: LocalDateTime,
    val description: String,
    val value: BigDecimal,
    val isPaidByEmployee: Boolean,
    val isValid: Boolean,
    private var _label: Label? = null,
) : LabelInterface, ModelObjectInterface<OutlayDto> {

    val label: Label?
        get() = _label

    companion object {

        /**
         * Extension function for list of [Outlay]'s to merge with a list of [Label]'s.
         *
         * Each expend in the list will have its label updated with
         * the corresponding from the labels list.
         *
         * @param labels A list of label objects.
         *
         * @return A list of expends with valid label.
         */
        fun List<Outlay>.merge(labels: List<Label>) {
            this.forEach { it.setLabelById(labels) }
        }

    }

    override fun setLabelById(labels: List<Label>) {
        if (labels.isEmpty()) throw EmptyDataException("Label list cannot be empty")

        _label = labels.firstOrNull { it.id == labelId }
            ?: throw NullLabelException("Label not found")
    }

    override fun getLabelName(): String {
        return try {
            label!!.name
        } catch (e: Exception) {
            ERROR_STRING
        }
    }

    override fun toDto() = OutlayDto(
        masterUid = masterUid,
        id = id,
        truckId = truckId,
        employeeId = employeeId,
        travelId = travelId,
        labelId = labelId,
        date = date.toDate(),
        value = value.toDouble(),
        description = description,
        company = company,
        isPaidByEmployee = isPaidByEmployee,
        isValid = isValid
    )

}

