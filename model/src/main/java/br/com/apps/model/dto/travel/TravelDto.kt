package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.interfaces.AccessPermissionInterface
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.user.AccessLevel
import br.com.apps.model.util.ACCESS_DENIED
import br.com.apps.model.util.toLocalDateTime
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [Travel].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class TravelDto(
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var employeeId: String? = null,
    var initialDate: Date? = null,
    var finalDate: Date? = null,
    val initialOdometerMeasurement: Double? = null,
    val finalOdometerMeasurement: Double? = null,
    @field:JvmField var isClosed: Boolean? = null,
    @field:JvmField var isFinished: Boolean? = null,
) : DtoObjectInterface<Travel>, AccessPermissionInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            truckId == null ||
            employeeId == null ||
            isFinished == null ||
            isClosed == null ||
            initialDate == null ||
            initialOdometerMeasurement == null
        ) throw CorruptedFileException("TravelDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            truckId == null ||
            employeeId == null ||
            isFinished == null ||
            isClosed == null ||
            initialDate == null ||
            finalDate == null ||
            initialOdometerMeasurement == null ||
            finalOdometerMeasurement == null
        ) throw InvalidForSavingException("TravelDto data is invalid: ($this)")
    }

    override fun toModel(): Travel {
        validateDataIntegrity()
        return Travel(
            masterUid = this.masterUid!!,
            id = this.id,
            truckId = this.truckId!!,
            employeeId = this.employeeId!!,
            initialDate = this.initialDate!!.toLocalDateTime(),
            finalDate = this.finalDate?.toLocalDateTime(),
            initialOdometerMeasurement = this.initialOdometerMeasurement!!.toBigDecimal(),
            finalOdometerMeasurement = this.finalOdometerMeasurement?.toBigDecimal(),
            isFinished = this.isFinished!!,
            isClosed = this.isClosed!!
        )
    }

    override fun validateWriteAccess(access: AccessLevel?) {
        if (access == null) throw NullPointerException()
        isFinished?.let {
            throw AccessLevelException(ACCESS_DENIED)
        } ?: throw NullPointerException()
    }

    override fun validateReadAccess() { }

}


