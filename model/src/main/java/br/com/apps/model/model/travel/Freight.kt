package br.com.apps.model.model.travel

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

data class Freight(
    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,
    val travelId: String? = null,
    val incomeId: String? = null,

    var origin: String? = null,
    var company: String? = null,
    var destiny: String? = null,
    var weight: BigDecimal? = null,
    var cargo: String? = null,
    var breakDown: BigDecimal? = null,
    var value: BigDecimal? = null,
    var loadingDate: LocalDateTime? = null,

    var dailyValue: BigDecimal? = null,
    var daily: Int? = null,
    var dailyTotalValue: BigDecimal? = null,

    @field:JvmField
    var isCommissionPaid: Boolean? = null,
    var commissionPercentual: BigDecimal? = null

) {

    fun getCommissionValue(): BigDecimal {
        return if (value != null && commissionPercentual != null) {
            val x = value!!.multiply(commissionPercentual)
            x.divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN)
        } else {
            BigDecimal.ZERO
        }
    }

    fun getTextDescription(): String {
        return destiny?.let { "Voce carregou para $it" } ?: "-"
    }

}
