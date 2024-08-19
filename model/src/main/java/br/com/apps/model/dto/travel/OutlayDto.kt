package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.interfaces.AccessPermissionInterface
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.util.ACCESS_DENIED
import br.com.apps.model.util.toLocalDateTime
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [Outlay].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class OutlayDto(
    // Ids
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var employeeId: String? = null,
    var travelId: String? = null,
    var labelId: String? = null,

    // Others
    var company: String? = null,
    var date: Date? = null,
    var description: String? = null,
    var value: Double? = null,
    @field:JvmField var isPaidByEmployee: Boolean? = null,
    @field:JvmField var isValid: Boolean? = null

) : DtoObjectInterface<Outlay>, AccessPermissionInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            truckId == null ||
            travelId == null ||
            employeeId == null ||
            labelId == null ||
            company == null ||
            description == null ||
            date == null ||
            value == null ||
            isPaidByEmployee == null ||
            isValid == null
        ) throw CorruptedFileException("OutlayDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            truckId == null ||
            travelId == null ||
            employeeId == null ||
            labelId == null ||
            company == null ||
            date == null ||
            description == null ||
            value == null ||
            isPaidByEmployee == null ||
            isValid == null
        ) throw InvalidForSavingException("OutlayDto data is invalid: ($this)")
    }

    override fun toModel(): Outlay {
        validateDataIntegrity()
        return Outlay(
            masterUid = masterUid!!,
            id = id!!,
            truckId = truckId!!,
            employeeId = employeeId!!,
            travelId = travelId!!,
            labelId = labelId!!,
            date = date!!.toLocalDateTime(),
            value = value!!.toBigDecimal(),
            description = description!!,
            company = company!!,
            isPaidByEmployee = isPaidByEmployee!!,
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