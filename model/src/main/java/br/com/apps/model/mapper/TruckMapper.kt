package br.com.apps.model.mapper

import br.com.apps.model.dto.fleet.TruckDto
import br.com.apps.model.model.fleet.FleetType
import br.com.apps.model.model.fleet.Truck
import java.math.BigDecimal


fun TruckDto.toModel(): Truck {
    this.validateDataIntegrity()
        return Truck(
            id = this.id,
            driverId = this.driverId!!,
            masterUid = this.masterUid!!,
            averageAim = this.averageAim!!,
            performanceAim = this.performanceAim!!,
            plate = this.plate ?: "-",
            color = this.color ?: "-",
            commissionPercentual = BigDecimal(this.commissionPercentual!!),
            fleetType = FleetType.getType(this.fleetType!!)
        )
    }
