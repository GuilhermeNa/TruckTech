package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

fun FreightDto.toModel(): Freight {
    return Freight(
        masterUid = this.masterUid,
        id = this.id,
        incomeId = this.incomeId,
        truckId = this.truckId,
        travelId = this.travelId,
        origin = this.origin,
        company = this.company,
        destiny = this.destiny,
        cargo = this.cargo,
        weight = this.weight?.toBigDecimal(),
        value = this.value?.toBigDecimal(),
        breakDown = this.breakDown?.let { BigDecimal(it) },
        loadingDate = this.loadingDate?.toLocalDateTime(),
        dailyValue = this.dailyValue?.let { BigDecimal(it) },
        daily = this.daily,
        dailyTotalValue = this.dailyTotalValue?.let { BigDecimal(it) },
        commissionPercentual = this.commissionPercentual?.let { BigDecimal(it) }
    )
}

fun Freight.toDto(): FreightDto {
    return FreightDto(
        masterUid = this.masterUid,
        id = this.id,
        incomeId = this.incomeId,
        truckId = this.truckId,
        travelId = this.travelId,
        origin = this.origin,
        company = this.company,
        destiny = this.destiny,
        cargo = this.cargo,
        weight = this.weight?.toDouble(),
        value = this.value?.toDouble(),
        breakDown = this.breakDown?.toDouble(),
        loadingDate = this.loadingDate?.toDate(),
        dailyValue = this.dailyValue?.toDouble(),
        daily = this.daily,
        dailyTotalValue = this.dailyTotalValue?.toDouble(),
        commissionPercentual = this.commissionPercentual?.toDouble()
    )
}