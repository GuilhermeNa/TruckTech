package br.com.apps.model.mapper

import br.com.apps.model.dto.payroll.TravelAidDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

fun TravelAidDto.toModel(): TravelAid {

    if(this.validateFields()) {
        return TravelAid(
            masterUid = this.masterUid!!,
            id = this.id,
            travelId = this.travelId!!,
            employeeId = this.employeeId!!,
            date = this.date!!.toLocalDateTime(),
            value = BigDecimal(this.value!!),
            isPaid = this.isPaid!!
        )
    }

    throw CorruptedFileException("CostHelpMapper, toModel: ($this)")
}

fun TravelAid.toDto(): TravelAidDto {
    return TravelAidDto(
        masterUid = this.masterUid,
        id = this.id,
        travelId = this.travelId,
        employeeId = this.employeeId,
        date = this.date.toDate(),
        value = this.value.toDouble(),
        isPaid = this.isPaid
    )
}