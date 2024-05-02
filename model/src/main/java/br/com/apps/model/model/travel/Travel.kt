package br.com.apps.model.model.travel

import java.math.BigDecimal
import java.time.LocalDateTime

data class Travel(
    val masterUid: String? = null,
    val id: String? = null,
    val truckId: String? = null,
    val driverId: String? = null,

    @field:JvmField
    val isFinished: Boolean? = false,
    val initialDate: LocalDateTime? = null,
    val finalDate: LocalDateTime? = null,
    val initialOdometerMeasurement: BigDecimal? = null,
    val finalOdometerMeasurement: BigDecimal? = null,

    var freightsList: List<Freight>? = null,
    var refuelsList: List<Refuel>? = null,
    var expendsList: List<Expend>? = null
) {

    fun getNumberOfFreights(): Int {
        return freightsList?.size ?: 0
    }

    fun getNumberOfRefuels(): Int {
        return refuelsList?.size ?: 0
    }

    fun getNumberOfExpends(): Int {
        return expendsList?.size ?: 0
    }

    fun getListOfIdsForFreightList(): List<String> {
        return freightsList?.mapNotNull { it.id } ?: emptyList()
    }

    fun getListOfIdsForRefuelList(): List<String> {
        return refuelsList?.mapNotNull { it.id } ?: emptyList()
    }

    fun getListOfIdsForExpendList(): List<String> {
        return expendsList?.mapNotNull { it.id } ?: emptyList()
    }

}