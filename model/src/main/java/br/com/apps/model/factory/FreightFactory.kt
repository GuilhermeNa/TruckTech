package br.com.apps.model.factory

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

object FreightFactory {

    fun create(dto: FreightDto): Freight {
        dto.validateDataIntegrity()
        return Freight(
            masterUid = dto.masterUid!!,
            truckId = dto.truckId!!,
            driverId = dto.driverId!!,
            travelId = dto.travelId!!,
            customerId = dto.customerId!!,

            origin = dto.origin!!,
            destiny = dto.destiny!!,
            weight = BigDecimal(dto.weight!!),
            cargo = dto.cargo!!,
            value = BigDecimal(dto.value!!),
            loadingDate = dto.loadingDate!!.toLocalDateTime(),

            isCommissionPaid = dto.isCommissionPaid!!,
            commissionPercentual = BigDecimal(dto.commissionPercentual!!),
            isValid = dto.isValid!!
        )
    }

    fun update(freight: Freight, viewDto: FreightDto) {
        viewDto.customerId?.run { freight.customerId = this }
        viewDto.destiny?.run { freight.destiny = this }
        viewDto.origin?.run { freight.origin = this }
        viewDto.cargo?.run { freight.cargo = this }
        viewDto.weight?.run { freight.weight = BigDecimal(this) }
        viewDto.value?.run { freight.value = BigDecimal(this) }
        viewDto.loadingDate?.run { freight.loadingDate = this.toLocalDateTime() }
        viewDto.breakDown?.run { freight.breakDown = BigDecimal(this) }
        viewDto.daily?.run { freight.daily = this }
        viewDto.dailyValue?.run { freight.dailyValue = BigDecimal(this) }
        viewDto.dailyTotalValue?.run { freight.dailyTotalValue = BigDecimal(this) }
        viewDto.commissionPercentual?.run { freight.commissionPercentual = BigDecimal(this) }
        viewDto.isValid?.run { freight.isValid = this }
    }

}
