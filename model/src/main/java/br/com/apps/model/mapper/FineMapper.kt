package br.com.apps.model.mapper

import br.com.apps.model.dto.FleetFineDto
import br.com.apps.model.model.FleetFine
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal


fun FleetFineDto.toModel(): FleetFine {
    this.validateDataIntegrity()
    return FleetFine(
        masterUid = this.masterUid!!,
        id = this.id!!,
        fleetId = this.fleetId!!,
        employeeId = this.employeeId!!,
        date = this.date?.toLocalDateTime()!!,
        description = this.description!!,
        code = this.code!!,
        value = BigDecimal(this.value!!)
    )
}

fun FleetFine.toDto(): FleetFineDto =
    FleetFineDto(
        masterUid = this.masterUid,
        id = this.id,
        fleetId = this.fleetId,
        employeeId = this.employeeId,
        date = this.date.toDate(),
        description = this.description,
        code = this.code,
        value = this.value.toDouble()
    )