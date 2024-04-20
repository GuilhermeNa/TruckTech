package br.com.apps.model.dto.travel

import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import java.util.Date

data class TravelDto(
    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,
    val initialDate: Date? = null,
    val finalDate: Date? = null,
    @field:JvmField
    val isFinished: Boolean? = false,

    val freightsList: List<Freight>? = null,
    val refuelsList: List<Refuel>? = null,
    val expendsList: List<Expend>? = null
) {


}


