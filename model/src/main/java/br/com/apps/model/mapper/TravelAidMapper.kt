package br.com.apps.model.mapper

import br.com.apps.model.dto.payroll.TravelAidDto
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

fun TravelAidDto.toModel(): TravelAid {
    this.validateDataIntegrity()
        return TravelAid(
            masterUid = this.masterUid!!,
            id = this.id,
            travelId = this.travelId!!,
            driverId = this.driverId!!,
            date = this.date!!.toLocalDateTime(),
            value = BigDecimal(this.value!!),
            isPaid = this.isPaid!!
        )
    }


fun TravelAid.toDto(): TravelAidDto =
     TravelAidDto(
        masterUid = this.masterUid,
        id = this.id,
        travelId = this.travelId,
        driverId = this.driverId,
        date = this.date.toDate(),
        value = this.value.toDouble(),
        isPaid = this.isPaid
    )
