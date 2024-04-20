package br.com.apps.trucktech.model.refuel

import java.math.BigDecimal
import java.time.LocalDateTime

data class ReFuel(

    val id: String,
    val fuelStationName: String,
    val date: LocalDateTime,
    val dieselValue: BigDecimal? = null,
    val dieselAmount: BigDecimal? = null,
    val arlaValue: BigDecimal? = null,
    val type: RefuelType

    ) {

    fun getTotalValue(): BigDecimal? {
        return when(type) {
            RefuelType.COMPLETE -> { dieselValue?.add(arlaValue) }
            RefuelType.DIESEL_ONLY -> { dieselValue }
            RefuelType.ARLA_ONLY -> { arlaValue }
        }
    }

}