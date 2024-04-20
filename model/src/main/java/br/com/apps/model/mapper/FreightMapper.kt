package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import java.math.BigDecimal

fun FreightDto.toModel(): Freight {
    return Freight(
        id = this.id,
        incomeId = this.incomeId,
        truckId = this.truckId,
        travelId = this.travelId,
        origin = this.origin,
        destiny = this.destiny,
        cargo = this.cargo,
        breakDown = this.breakDown?.let { BigDecimal(it) },
        date = this.date?.toLocalDateTime(),
    )
}