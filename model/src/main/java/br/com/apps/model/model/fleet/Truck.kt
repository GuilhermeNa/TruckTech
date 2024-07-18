package br.com.apps.model.model.fleet

import java.math.BigDecimal

data class Truck(
    override val masterUid: String,
    override val id: String? = null,
    override val plate: String,
    override val fleetType: FleetType,
    val driverId: String,
    val averageAim: Double,
    val performanceAim: Double,
    val color: String,
    val commissionPercentual: BigDecimal,
    var trailerList: List<Trailer>? = null
) : Fleet(
    masterUid = masterUid,
    id = id,
    plate = plate,
    fleetType = fleetType
) {

    /**
     * Retrieves a list of all fleet IDs associated with the truck, including its own ID
     * and the IDs of any trailers attached to it.
     *
     * @return List of fleet IDs as [String].
     */
    fun getFleetIds(): List<String> {
        val fleetIds = mutableListOf<String>()
        id?.let { fleetIds.add(it) }
        trailerList?.forEach { t -> t.id?.let { fleetIds.add(it) } }
        return fleetIds
    }

}