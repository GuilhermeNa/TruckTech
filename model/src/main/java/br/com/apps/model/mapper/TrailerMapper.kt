package br.com.apps.model.mapper

import br.com.apps.model.dto.fleet.TrailerDto
import br.com.apps.model.model.fleet.FleetType
import br.com.apps.model.model.fleet.Trailer

fun TrailerDto.toModel(): Trailer {
   this.validateDataIntegrity()
        return Trailer(
            masterUid = this.masterUid!!,
            id = this.id,
            plate = this.plate!!,
            fleetType = FleetType.getType(this.fleetType!!),
            truckId = this.truckId
        )
    }
