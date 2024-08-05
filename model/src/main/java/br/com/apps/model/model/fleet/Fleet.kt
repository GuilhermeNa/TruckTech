package br.com.apps.model.model.fleet

import br.com.apps.model.enums.FleetCategory

abstract class Fleet(
    open val masterUid: String,
    open val id: String,
    open val plate: String,
    open val fleetType: FleetCategory
)



