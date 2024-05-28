package br.com.apps.model.mapper

import br.com.apps.model.dto.TruckDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.model.Truck
import java.math.BigDecimal


fun TruckDto.toModel(): Truck {

    if(this.validateFields()) {
        return Truck(
            id = this.id,
            driverId = this.driverId!!,
            masterUid = this.masterUid!!,
            plate = this.plate ?: "-",
            color = this.color ?: "-",
            commissionPercentual = BigDecimal(this.commissionPercentual!!)
        )
    }

    throw CorruptedFileException("TruckMapper, toModel ($this)")

}
