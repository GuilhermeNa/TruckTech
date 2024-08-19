package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.interfaces.AccessPermissionInterface
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.util.ACCESS_DENIED
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [TravelAid].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class TravelAidDto(
    var masterUid: String? = null,
    var id: String? = null,
    var employeeId: String? = null,
    var travelId: String? = null,
    var date: Date? = null,
    var value: Double? = null,
    @field:JvmField var isValid: Boolean? = null
) : DtoObjectInterface<TravelAid>, AccessPermissionInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            employeeId == null ||
            travelId == null ||
            date == null ||
            value == null ||
            isValid == null
        ) throw CorruptedFileException("TravelAidDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {}

    override fun toModel(): TravelAid {
        validateDataIntegrity()
        return TravelAid(
            masterUid = masterUid!!,
            id = id!!,
            travelId = travelId!!,
            employeeId = employeeId!!,
            date = date!!.toLocalDateTime(),
            value = BigDecimal(value!!),
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