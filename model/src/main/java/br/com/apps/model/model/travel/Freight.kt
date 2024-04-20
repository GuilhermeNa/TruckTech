package br.com.apps.model.model.travel

import java.math.BigDecimal
import java.time.LocalDateTime

data class Freight(
    val id: String? = null,
    val incomeId: String? = null,
    val truckId: String? = null,
    val travelId: String? = null,

    val origin: String? = null,
    val destiny: String? = null,
    val cargo: String? = null,
    val breakDown: BigDecimal? = null,
    val date: LocalDateTime? = null,
    val type: FreightType? = null
) {


}

enum class FreightType {
    DEFAULT, COMPLEMENT
}