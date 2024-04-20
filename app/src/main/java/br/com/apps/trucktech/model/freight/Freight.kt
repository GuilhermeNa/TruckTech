package br.com.apps.trucktech.model.freight

import java.math.BigDecimal
import java.time.LocalDateTime

data class Freight(

    val id: String,
    val company: String,
    val origin: String,
    val destiny: String,
    val cargo: String,
    val value: BigDecimal,
    val breakDown: BigDecimal? = null,
    val date: LocalDateTime,
    val type: FreightType
) {


}