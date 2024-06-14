package br.com.apps.model.mapper

import br.com.apps.model.dto.payroll.TravelAdvanceDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.payroll.TravelAdvance
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

fun TravelAdvanceDto.toModel(): TravelAdvance {

    if(this.validateFields()) {
        return TravelAdvance(
            masterUid = this.masterUid!!,
            id = this.id,
            employeeId = this.employeeId!!,
            date = this.date!!.toLocalDateTime(),
            value = BigDecimal(this.value!!),
            isPaid = this.isPaid!!
        )
    }

    throw CorruptedFileException("CostHelpMapper, toModel: ($this)")
}

fun TravelAdvance.toDto(): TravelAdvanceDto {
    return TravelAdvanceDto(
        masterUid = this.masterUid,
        id = this.id,
        employeeId = this.employeeId,
        date = this.date.toDate(),
        value = this.value.toDouble(),
        isPaid = this.isPaid
    )
}