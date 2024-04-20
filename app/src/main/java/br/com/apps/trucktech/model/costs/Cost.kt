package br.com.apps.trucktech.model.costs

import java.math.BigDecimal
import java.time.LocalDateTime

abstract class Cost(

    open val id: String,

    open val date: LocalDateTime,
    open val value: BigDecimal,
    open val company: String?,
    open val description: String? = null,
    open val urlImage: String? = null,
    open val label: Long

)