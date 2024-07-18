package br.com.apps.model.mapper

import br.com.apps.model.dto.FineDto
import br.com.apps.model.model.Fine
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal


fun FineDto.toModel(): Fine {
    return Fine(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        date = this.date?.toLocalDateTime(),
        description = this.description,
        code = this.code,
        value = this.value?.let { BigDecimal(it) }
    )
}

fun Fine.toDto(): FineDto =
    FineDto(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        driverId = this.driverId,
        date = this.date?.toDate(),
        description = this.description,
        code = this.code,
        value = this.value?.toDouble()
    )