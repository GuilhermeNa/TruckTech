package br.com.apps.model.mapper

import br.com.apps.model.dto.TruckDto
import br.com.apps.model.model.Truck


fun TruckDto.toModel(): Truck {
    return Truck(
        id = this.id,
        driverId = this.driverId,
        masterUid = this.masterUid,
        plate = this.plate,
        color = this.color
    )
}
