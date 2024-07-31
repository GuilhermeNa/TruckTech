package br.com.apps.model.mapper

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.toDate
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

fun FreightDto.toModel(): Freight {
    this.validateDataIntegrity()
    return Freight(
        masterUid = this.masterUid!!,
        id = this.id!!,
        truckId = this.truckId!!,
        travelId = this.travelId!!,
        employeeId = this.employeeId!!,
        customerId = this.customerId!!,
        origin = this.origin!!,
        destiny = this.destiny!!,
        cargo = this.cargo!!,
        weight = this.weight?.toBigDecimal()!!,
        value = this.value!!.toBigDecimal(),
        loadingDate = this.loadingDate!!.toLocalDateTime(),
        commissionPercentual = this.commissionPercentual?.let { BigDecimal(it) }!!,
        isCommissionPaid = this.isCommissionPaid!!,
        isValid = this.isValid!!
    )
}

fun Freight.toDto(): FreightDto =
    FreightDto(
        masterUid = this.masterUid,
        id = this.id,
        truckId = this.truckId,
        travelId = this.travelId,
        employeeId = this.employeeId,
        customerId = this.customerId,
        origin = this.origin,
        destiny = this.destiny,
        cargo = this.cargo,
        weight = this.weight.toDouble(),
        value = this.value.toDouble(),
        loadingDate = this.loadingDate.toDate(),
        commissionPercentual = this.commissionPercentual.toDouble(),
        isCommissionPaid = this.isCommissionPaid,
        isValid = this.isValid
    )
