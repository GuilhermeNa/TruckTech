package br.com.apps.model.factory

import br.com.apps.model.factory.FactoryUtil.Companion.checkIfStringsAreBlank
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal
import java.security.InvalidParameterException

object ExpendFactory {

    const val TAG_MASTER_UID = "masterUid"
    const val TAG_TRUCK_ID = "truckId"
    const val TAG_DRIVER_ID = "driverId"
    const val TAG_TRAVEL_ID = "travelId"
    const val TAG_LABEL_ID = "labelId"

    const val TAG_COMPANY = "company"
    const val TAG_DATE = "date"
    const val TAG_DESCRIPTION = "description"
    const val TAG_VALUE = "value"
    const val TAG_PAID_BY_EMPLOYEE = "paidByEmployee"
    private const val TAG_ALREADY_REFUNDED = "alreadyRefunded"

    fun create(mappedFields: HashMap<String, String>): Expend {
        val masterUid = mappedFields[TAG_MASTER_UID]
            ?: throw NullPointerException("ExpendFactory, create: masterUid is null")

        val travelId = mappedFields[TAG_TRAVEL_ID]
            ?: throw NullPointerException("ExpendFactory, create: travelId is null")

        val truckId = mappedFields[TAG_TRUCK_ID]
            ?: throw NullPointerException("ExpendFactory, create: truckId is null")

        val driverId = mappedFields[TAG_DRIVER_ID]
            ?: throw NullPointerException("ExpendFactory, create: driverId is null")

        val labelId = mappedFields[TAG_LABEL_ID]
            ?: throw NullPointerException("ExpendFactory, create: labelId is null")

        val company = mappedFields[TAG_COMPANY]
            ?: throw NullPointerException("ExpendFactory, create: company is null")

        val date = mappedFields[TAG_DATE]
            ?: throw NullPointerException("ExpendFactory, create: date is null")

        val description = mappedFields[TAG_DESCRIPTION]
            ?: throw NullPointerException("ExpendFactory, create: description is null")

        val value = mappedFields[TAG_VALUE]
            ?: throw NullPointerException("ExpendFactory, create: value is null")

        val paidByEmployee = mappedFields[TAG_PAID_BY_EMPLOYEE]
            ?: throw NullPointerException("ExpendFactory, create: paidByEmployee is null")

        checkIfStringsAreBlank(
            masterUid, travelId, truckId, driverId, labelId,
            date, description, value, paidByEmployee
        )

        return Expend(
            masterUid = masterUid,
            truckId = truckId,
            driverId = driverId,
            travelId = travelId,
            labelId = labelId,

            company = company,
            date = date.toLocalDateTime(),
            description = description,
            value = BigDecimal(value),
            paidByEmployee = paidByEmployee.toBoolean(),
            alreadyRefunded = false
        )

    }

    fun update(expend: Expend, mappedFields: HashMap<String, String>) {
        mappedFields.forEach { (key, value) ->
            when (key) {
                TAG_DATE -> expend.date = value.toLocalDateTime()
                TAG_COMPANY -> expend.company = value
                TAG_DESCRIPTION -> expend.description = value
                TAG_VALUE -> expend.value = BigDecimal(value)
                TAG_LABEL_ID -> expend.labelId = value
                TAG_PAID_BY_EMPLOYEE -> expend.paidByEmployee = value.toBoolean()
                TAG_ALREADY_REFUNDED -> expend.paidByEmployee = value.toBoolean()
                else -> throw InvalidParameterException("Invalid key")
            }
        }
    }

}