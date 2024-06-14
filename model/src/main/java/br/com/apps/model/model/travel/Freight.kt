package br.com.apps.model.model.travel

import br.com.apps.model.model.Customer
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

data class Freight(
    val masterUid: String,
    val id: String? = null,
    val truckId: String,
    val driverId: String,
    val travelId: String,
    var customerId: String,

    var customer: Customer? = null,
    var origin: String,
    var destiny: String,
    var weight: BigDecimal,
    var cargo: String,

    var breakDown: BigDecimal? = null,
    var value: BigDecimal? = null,
    var loadingDate: LocalDateTime? = null,

    var dailyValue: BigDecimal? = null,
    var daily: Int? = null,
    var dailyTotalValue: BigDecimal? = null,

    @field:JvmField
    var isCommissionPaid: Boolean,
    var commissionPercentual: BigDecimal,

    @field:JvmField
    var isValid: Boolean

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
