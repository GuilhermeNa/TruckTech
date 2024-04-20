package br.com.apps.trucktech.model.costs

import java.math.BigDecimal
import java.time.LocalDateTime

data class DefaultCost(

    override val id: String,

    override val date: LocalDateTime,
    override val value: BigDecimal,
    override val company: String?,
    override val description: String?,
    override val urlImage: String? = null,
    override val label: Long

) : Cost(
    id = id,
    date = date,
    value = value,
    company = company,
    description = description,
    urlImage = urlImage,
    label = label
) {







}