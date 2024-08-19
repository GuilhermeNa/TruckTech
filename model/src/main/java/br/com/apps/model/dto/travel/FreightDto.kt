package br.com.apps.model.dto.travel

import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.interfaces.AccessPermissionInterface
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.util.ACCESS_DENIED
import br.com.apps.model.util.toLocalDateTime
import java.math.BigDecimal
import java.util.Date

/**
 * Data Transfer Object (DTO) representing a [Freight].
 *
 * This class is used to transfer information between different parts
 * of the application or between different systems. It plays a crucial role in
 * communicating with the database by being used to send and receive data from
 * the database.
 */
data class FreightDto(
    // Ids
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var travelId: String? = null,
    var employeeId: String? = null,
    var customerId: String? = null,

    // Others
    var cargo: String? = null,
    var origin: String? = null,
    var destiny: String? = null,
    var value: Double? = null,
    var weight: Double? = null,
    var loadingDate: Date? = null,
    var commissionPercentual: Double? = null,
    @field:JvmField var isValid: Boolean? = null
) : DtoObjectInterface<Freight>, AccessPermissionInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            truckId == null ||
            travelId == null ||
            employeeId == null ||
            customerId == null ||
            cargo == null ||
            origin == null ||
            destiny == null ||
            value == null ||
            weight == null ||
            loadingDate == null ||
            commissionPercentual == null ||
            isValid == null
        ) throw CorruptedFileException("FreightDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            truckId == null ||
            travelId == null ||
            employeeId == null ||
            customerId == null ||
            origin == null ||
            destiny == null ||
            weight == null ||
            cargo == null ||
            value == null ||
            loadingDate == null ||
            commissionPercentual == null ||
            isValid == null
        ) throw InvalidForSavingException("FreightDto data is invalid: ($this)")
    }

    override fun toModel(): Freight {
        validateDataIntegrity()
        return Freight(
            masterUid = masterUid!!,
            id = id!!,
            truckId = truckId!!,
            travelId = travelId!!,
            employeeId = employeeId!!,
            customerId = customerId!!,
            origin = origin!!,
            destiny = destiny!!,
            cargo = cargo!!,
            weight = weight?.toBigDecimal()!!,
            value = value!!.toBigDecimal(),
            loadingDate = loadingDate!!.toLocalDateTime(),
            commissionPercentual = commissionPercentual?.let { BigDecimal(it) }!!,
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