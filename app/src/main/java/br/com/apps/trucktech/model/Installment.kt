package br.com.apps.trucktech.model

import java.math.BigDecimal

data class Installment(

    val id: Long,
    val foreignKey: Long,
    val value: BigDecimal,
    val number: Int

)
