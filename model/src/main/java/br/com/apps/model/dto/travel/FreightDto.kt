package br.com.apps.model.dto.travel

import br.com.apps.model.model.travel.Complement
import java.util.Date

data class FreightDto(
    var masterUid: String? = null,
    var id: String? = null,
    val truckId: String? = null,
    val travelId: String? = null,
    val driverId: String? = null,
    val incomeId: String? = null,

    val origin: String? = null,
    val company: String? = null,
    val destiny: String? = null,
    val weight: Double? = null,
    val cargo: String? = null,
    val value: Double? = null,
    val breakDown: Double? = null,
    val loadingDate: Date? = null,

    val dailyValue: Double? = null,
    val daily: Int? = null,
    val dailyTotalValue: Double? = null,
    val complement: List<Complement>? = null,

    @field:JvmField
    var isCommissionPaid: Boolean? = null,
    var commissionPercentual: Double? = null

) {

    fun validateFields(): Boolean {
        var isValid = true

        if (masterUid == null || truckId == null || travelId == null || driverId == null || origin == null ||
            company == null || destiny == null || weight == null || cargo == null || value == null ||
            loadingDate == null || isCommissionPaid == null || commissionPercentual == null
        ) {
            isValid = false
        }

        return isValid
    }

}