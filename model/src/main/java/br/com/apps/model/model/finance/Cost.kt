package br.com.apps.model.model.finance

import java.math.BigDecimal


class Cost (
    val id: Long = 0L,
    val truckId: Long = 0L,
    val driverId: Long = 0L,
    val labelId: Long = 0L,

    val value: BigDecimal = BigDecimal.ZERO,
    val type: CostType
) {

}

enum class CostType {
    FIXED, VARIABLE
}
