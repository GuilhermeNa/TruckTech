package br.com.apps.model.model.travel

import br.com.apps.model.expressions.toPercentValue
import br.com.apps.model.model.Customer
import java.math.BigDecimal
import java.time.LocalDateTime

data class Freight(
    val masterUid: String,
    var id: String? = null,
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
    var value: BigDecimal,
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

    /**
     * Calculates and returns the commission value based on the freight value
     * and commission percentage.
     *
     * @return The calculated commission value as a BigDecimal.
     */
    fun getCommissionValue(): BigDecimal {
        val x = value.multiply(commissionPercentual)
        return x.toPercentValue()
    }

    /**
     * Retrieves a text description based on the freight destination.
     *
     * @return A text description stating the freight destination.
     */
    fun getTextDescription(): String {
        return destiny.let { "Voce carregou para $it" } ?: "-"
    }

}
