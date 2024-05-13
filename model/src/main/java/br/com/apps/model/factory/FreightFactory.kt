package br.com.apps.model.factory

import br.com.apps.model.factory.FactoryUtil.Companion.checkIfStringsAreBlank
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal
import java.security.InvalidParameterException

object FreightFactory {

    const val TAG_MASTER_UID = "masterUid"
    const val TAG_TRAVEL_ID = "travelId"
    const val TAG_TRUCK_ID = "truckId"
    const val TAG_DRIVER_ID = "driverId"

    const val TAG_LOADING_DATE = "loadingDate"
    const val TAG_ORIGIN = "origin"
    const val TAG_COMPANY = "company"
    const val TAG_DESTINY = "destiny"
    const val TAG_WEIGHT = "weight"
    const val TAG_CARGO = "cargo"
    const val TAG_VALUE = "value"

    private const val TAG_BREAKDOWN = "breakDown"
    private const val TAG_DAILY_VALUE = "dailyValue"
    private const val TAG_DAILY = "daily"
    private const val TAG_DAILY_TOTAL_VALUE = "dailyTotalValue"

    fun create(mappedFields: HashMap<String, String>): Freight {
        val masterUid = mappedFields[TAG_MASTER_UID]
            ?: throw NullPointerException("FreightFactory, create: masterUid is null")

        val travelId = mappedFields[TAG_TRAVEL_ID]
            ?: throw NullPointerException("FreightFactory, create: travelId is null")

        val truckId = mappedFields[TAG_TRUCK_ID]
            ?: throw NullPointerException("FreightFactory, create: truckId is null")

        val driverId = mappedFields[TAG_DRIVER_ID]
            ?: throw NullPointerException("FreightFactory, create: driverId is null")

        val origin = mappedFields[TAG_ORIGIN]
            ?: throw NullPointerException("FreightFactory, create: origin is null")

        val company = mappedFields[TAG_COMPANY]
            ?: throw NullPointerException("FreightFactory, create: company is null")

        val destiny = mappedFields[TAG_DESTINY]
            ?: throw NullPointerException("FreightFactory, create: destiny is null")

        val weight = mappedFields[TAG_WEIGHT]
            ?: throw NullPointerException("FreightFactory, create: weight is null")

        val cargo = mappedFields[TAG_CARGO]
            ?: throw NullPointerException("FreightFactory, create: cargo is null")

        val value = mappedFields[TAG_VALUE]
            ?: throw NullPointerException("FreightFactory, create: value is null")

        val loadingDate = mappedFields[TAG_LOADING_DATE]
            ?: throw NullPointerException("FreightFactory, create: loadingDate is null")

        checkIfStringsAreBlank(
            masterUid, travelId, truckId, driverId, origin,
            company, destiny, weight, cargo, value, loadingDate
        )

        return Freight(
            masterUid = masterUid,
            truckId = truckId,
            driverId = driverId,
            travelId = travelId,

            loadingDate = loadingDate.toLocalDateTime(),
            origin = origin,
            company = company,
            destiny = destiny,
            weight = BigDecimal(weight),
            cargo = cargo,
            value = BigDecimal(value),

            isCommissionPaid = false
        )

    }

    fun update(freight: Freight, mappedFields: HashMap<String, String>) {
        mappedFields.forEach { (key, value) ->

            checkIfStringsAreBlank(value)

            when (key) {
                TAG_ORIGIN -> freight.origin = value
                TAG_COMPANY -> freight.company = value
                TAG_DESTINY -> freight.destiny = value
                TAG_WEIGHT -> freight.weight = BigDecimal(value)
                TAG_CARGO -> freight.cargo = value
                TAG_BREAKDOWN -> freight.breakDown = BigDecimal(value)
                TAG_VALUE -> freight.value = BigDecimal(value)
                TAG_DAILY_VALUE -> freight.dailyValue = BigDecimal(value)
                TAG_DAILY -> freight.daily = value.toInt()
                TAG_DAILY_TOTAL_VALUE -> freight.dailyTotalValue = BigDecimal(value)
                TAG_LOADING_DATE -> freight.loadingDate = value.toLocalDateTime()
                else -> throw InvalidParameterException(
                    "FreightFactory, update: Impossible update this field ($key)"
                )
            }
        }
    }

}