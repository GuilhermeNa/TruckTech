package br.com.apps.model.model.travel

import java.math.BigDecimal
import java.security.InvalidParameterException
import java.time.LocalDateTime

data class Freight(
    val masterUid: String? = null,
    val id: String? = null,
    val incomeId: String? = null,
    val truckId: String? = null,
    val travelId: String? = null,

    var origin: String? = null,
    var company: String? = null,
    var destiny: String? = null,
    var weight: BigDecimal? = null,
    var cargo: String? = null,
    var breakDown: BigDecimal? = null,
    var value: BigDecimal? = null,
    val loadingDate: LocalDateTime? = null,

    var dailyValue: BigDecimal? = null,
    var daily: Int? = null,
    var dailyTotalValue: BigDecimal? = null

) {

    fun getTextDescription(): String {
        return destiny?.let { "Voce carregou para $it" } ?: "-"
    }

    fun updateFields(mappedFields: HashMap<String, String>) {
        mappedFields.forEach { (key, value) ->
            when (key) {
                TAG_ORIGIN -> this.origin = value
                TAG_COMPANY -> this.company = value
                TAG_DESTINY -> this.destiny = value
                TAG_WEIGHT -> this.weight = BigDecimal(value)
                TAG_CARGO -> this.cargo = value
                TAG_BREAKDOWN -> this.breakDown = BigDecimal(value)
                TAG_VALUE -> this.value = BigDecimal(value)
                TAG_DAILY_VALUE -> this.dailyValue = BigDecimal(value)
                TAG_DAILY -> this.daily = value.toInt()
                TAG_DAILY_TOTAL_VALUE -> this.dailyTotalValue = BigDecimal(value)
                else -> throw InvalidParameterException("Impossible update this field")
            }
        }
    }

    companion object {
        const val TAG_ORIGIN = "origin"
        const val TAG_COMPANY = "company"
        const val TAG_DESTINY = "destiny"
        const val TAG_WEIGHT = "weight"
        const val TAG_CARGO = "cargo"
        const val TAG_BREAKDOWN = "breakDown"
        const val TAG_VALUE = "value"
        const val TAG_LOADING_DATE= "loadingDate"
        const val TAG_DAILY_VALUE = "dailyValue"
        const val TAG_DAILY = "daily"
        const val TAG_DAILY_TOTAL_VALUE = "dailyTotalValue"
    }

}
