package br.com.apps.model.mapper

import br.com.apps.model.dto.payroll.AdvanceDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

fun AdvanceDto.toModel(): Advance {

    if (this.validateFields()) {
        return Advance(
            masterUid = this.masterUid!!,
            id = this.id,
            employeeId = this.employeeId!!,
            date = this.date!!.toLocalDateTime(),
            value = BigDecimal(this.value!!),
            isPaid = this.isPaid
        )
    }

    throw CorruptedFileException("AdvanceDtoMapper, toModel: ($this)")
}

fun Advance.toDto(): AdvanceDto {
    return AdvanceDto(
        masterUid = this.masterUid,
        id = this.id,
        employeeId = this.employeeId,
        date = this.date?.toDate(),
        value = this.value?.toDouble(),
        isPaid = this.isPaid
    )
}