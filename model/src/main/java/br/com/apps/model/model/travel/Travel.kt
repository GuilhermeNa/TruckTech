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
    var initialOdometerMeasurement: BigDecimal? = null,
    var finalOdometerMeasurement: BigDecimal? = null,

    var freightsList: List<Freight>? = null,
    var refuelsList: List<Refuel>? = null,
    var expendsList: List<Expend>? = null
) {

    fun getListSize(listTag: Int): Int {
        return when (listTag) {
            FREIGHT -> freightsList?.size ?: 0
            EXPEND -> expendsList?.size ?: 0
            REFUEL -> refuelsList?.size ?: 0
            else -> 0
        }
    }

    fun getListOfIdsForList(listTag: Int): List<String> {
        return when (listTag) {
            FREIGHT -> freightsList?.mapNotNull { it.id } ?: emptyList()
            EXPEND -> expendsList?.mapNotNull { it.id } ?: emptyList()
            REFUEL -> refuelsList?.mapNotNull { it.id } ?: emptyList()
            else -> emptyList()
        }
    }

    fun getDifferenceBetweenInitialAndFinalOdometerMeasure(): BigDecimal {
        return if (initialOdometerMeasurement != null
            && finalOdometerMeasurement != null
        ) {
            finalOdometerMeasurement!!.subtract(initialOdometerMeasurement)

        } else {
            BigDecimal.ZERO

        }
    }

    fun checkDatesOrder(): Boolean {
        if (initialDate != null && finalDate != null) {
            return initialDate.isBefore(finalDate)
        }

        return false
    }

    fun checkForDuplicatedItems(): Boolean {
        freightsList?.let { freights ->
            val uniqueIds = mutableSetOf<String>()

            freights.mapNotNull {it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    return true
                } else {
                    uniqueIds.add(id)
                }
            }
        }

        refuelsList?.let { refuels ->
            val uniqueIds = mutableSetOf<String>()

            refuels.mapNotNull {it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    return true
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        expendsList?.let { expends ->
            val uniqueIds = mutableSetOf<String>()

            expends.mapNotNull {it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    return true
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        return false
    }

    fun validateIds(): Boolean {
        return !(masterUid.isNullOrBlank() ||
                id.isNullOrBlank() ||
                truckId.isNullOrBlank() ||
                driverId.isNullOrBlank())
    }

    companion object {
        const val FREIGHT = 0
        const val EXPEND = 1
        const val REFUEL = 2
    }

}