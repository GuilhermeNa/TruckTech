package br.com.apps.model.factory

import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.toLocalDateTime
import java.math.BigDecimal

object ExpendFactory {

    fun create(viewDto: ExpendDto): Expend {
        viewDto.validateDataIntegrity()
        return Expend(
            masterUid = viewDto.masterUid!!,
            truckId = viewDto.truckId!!,
            driverId = viewDto.driverId!!,
            travelId = viewDto.travelId!!,
            labelId = viewDto.labelId!!,
            label = viewDto.label,
            company = viewDto.company!!,
            date = viewDto.date!!.toLocalDateTime(),
            description = viewDto.description!!,
            value = BigDecimal(viewDto.value!!),
            isPaidByEmployee = viewDto.isPaidByEmployee!!,
            isAlreadyRefunded = viewDto.isAlreadyRefunded!!,
            isValid = viewDto.isValid!!
        )
    }

    fun update(expend: Expend, viewDto: ExpendDto) {
        viewDto.labelId?.let { expend.labelId = it }
        viewDto.company?.let { expend.company = it }
        viewDto.date?.let { expend.date = it.toLocalDateTime() }
        viewDto.description?.let { expend.description = it }
        viewDto.value?.let { expend.value = BigDecimal(it) }
        viewDto.isPaidByEmployee?.let { expend.isPaidByEmployee = it }
        viewDto.isAlreadyRefunded?.let { expend.isAlreadyRefunded = it }
        viewDto.isValid?.let { expend.isValid = it }
        expend.label = viewDto.label
    }

}