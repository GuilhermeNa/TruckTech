package br.com.apps.model.dto.travel

import br.com.apps.model.dto.DtoInterface
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidAuthLevelException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.travel.Complement
import br.com.apps.model.model.user.PermissionLevelType
import java.util.Date

data class FreightDto(
    var masterUid: String? = null,
    var id: String? = null,
    var truckId: String? = null,
    var travelId: String? = null,
    var driverId: String? = null,
    var customerId: String? = null,

    var customer: String? = null,
    var origin: String? = null,
    var destiny: String? = null,
    var cargo: String? = null,
    var weight: Double? = null,
    var value: Double? = null,
    val breakDown: Double? = null,
    var loadingDate: Date? = null,

    val dailyValue: Double? = null,
    val daily: Int? = null,
    val dailyTotalValue: Double? = null,
    val complement: List<Complement>? = null,

    @field:JvmField
    var isCommissionPaid: Boolean? = null,
    var commissionPercentual: Double? = null,

    @field:JvmField
    var isValid: Boolean? = null

) : DtoInterface {

    override fun validateDataIntegrity() {

        if (masterUid == null ||
            truckId == null ||
            travelId == null ||
            driverId == null ||
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
        ) throw CorruptedFileException("FreightDto data is corrupted: ($this)")
    }

    override fun validateForDataBaseInsertion() {
        if (masterUid == null ||
            truckId == null ||
            travelId == null ||
            driverId == null ||
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