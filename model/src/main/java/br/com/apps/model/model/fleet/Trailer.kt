package br.com.apps.model.model.fleet

data class Trailer(
    override val masterUid: String,
    override var id: String? = null,
    override val plate: String,
    override val fleetType: FleetType,
    val truckId: String? = null,
) : Fleet(
    masterUid = masterUid,
    id = id,
    plate = plate,
    fleetType = fleetType
)