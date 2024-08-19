package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.interfaces.AccessPermissionInterface
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.util.ACCESS_DENIED
import br.com.apps.model.util.toLocalDateTime
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [Refuel].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class RefuelDto(
    // Ids
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var travelId: String? = null,

    // Others
    var date: Date? = null,
    var station: String? = null,
    var odometerMeasure: Double? = null,
    var valuePerLiter: Double? = null,
    var amountLiters: Double? = null,
    var totalValue: Double? = null,
    @field:JvmField var isCompleteRefuel: Boolean? = null,
    @field:JvmField var isValid: Boolean? = null
) : DtoObjectInterface<Refuel>, AccessPermissionInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            truckId == null ||
            travelId == null ||
            date == null ||
            station == null ||
            odometerMeasure == null ||
            valuePerLiter == null ||
            amountLiters == null ||
            totalValue == null ||
            isCompleteRefuel == null ||
            isValid == null
        ) throw CorruptedFileException("RefuelDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            truckId == null ||
            travelId == null ||
            date == null ||
            station == null ||
            odometerMeasure == null ||
            valuePerLiter == null ||
            amountLiters == null ||
            totalValue == null ||
            isCompleteRefuel == null ||
            isValid == null
        ) throw InvalidForSavingException("RefuelDto data is invalid: ($this)")
    }

    override fun toModel(): Refuel {
        validateDataIntegrity()
        return Refuel(
            masterUid = masterUid!!,
            id = id!!,
            truckId = truckId!!,
            travelId = travelId,
            date = date!!.toLocalDateTime(),
            station = station!!,
            odometerMeasure = odometerMeasure!!.toBigDecimal(),
            valuePerLiter = valuePerLiter!!.toBigDecimal(),
            amountLiters = amountLiters!!.toBigDecimal(),
            totalValue = totalValue!!.toBigDecimal(),
            isCompleteRefuel = isCompleteRefuel!!,
            isValid = isValid!!
        )
    }

    override fun validateWriteAccess(access: AccessLevel?) {
        if (access == null) throw NullPointerException()
        isValid?.let {
            throw AccessLevelException(ACCESS_DENIED)
        } ?: throw NullPointerException()
    }

    override fun validateReadAccess() {
        TODO("Not yet implemented")
    }

}