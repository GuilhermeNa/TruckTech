package br.com.apps.model.dto.travel

import br.com.apps.model.dto.DtoObjectsInterface
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidAuthLevelException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.user.PermissionLevelType
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
    @field:JvmField
    var isValid: Boolean? = null,
    @field:JvmField
    var isCommissionPaid: Boolean? = null,

) : DtoObjectsInterface {

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
            isCommissionPaid == null ||
            isValid == null
        ) throw CorruptedFileException("FreightDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {
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
            isCommissionPaid == null ||
            commissionPercentual == null ||
            isValid == null
        ) throw InvalidForSavingException("FreightDto data is invalid: ($this)")
    }

    fun validatePermission(authLevel: PermissionLevelType?) {
        if (authLevel == null) throw NullPointerException("AuthLevel is null")
        if (isValid == null) throw NullPointerException("isValid is null")
        if (isValid!! && authLevel != PermissionLevelType.MANAGER) throw InvalidAuthLevelException()
    }

}