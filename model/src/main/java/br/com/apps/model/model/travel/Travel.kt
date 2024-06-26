package br.com.apps.model.model.travel

import br.com.apps.model.exceptions.EmptyDataException
import java.math.BigDecimal
import java.security.InvalidParameterException
import java.time.LocalDateTime

data class Travel(
    val masterUid: String,
    val id: String? = null,
    val truckId: String,
    val driverId: String,

    @field:JvmField
    var isFinished: Boolean,

    val initialDate: LocalDateTime,
    var finalDate: LocalDateTime? = null,

    var initialOdometerMeasurement: BigDecimal,
    var finalOdometerMeasurement: BigDecimal? = null,

    var freightsList: List<Freight>? = null,
    var refuelsList: List<Refuel>? = null,
    var expendsList: List<Expend>? = null,
    var aidList: List<TravelAid>? = null

) {

    fun getCommissionValue(): BigDecimal {
        var commission = BigDecimal.ZERO

        freightsList?.let { freights ->
            freights.forEach { f ->
                commission = commission.add(f.getCommissionValue())
            }
        }

        return commission
    }

    fun getTravelAuthenticationPercent(): Double {
        val authenticableItems = getListSize(FREIGHT) + getListSize(EXPEND) + getListSize(REFUEL)
        var authenticated = 0.0

        freightsList?.forEach { if (it.isValid) authenticated++ }
        refuelsList?.forEach { if (it.isValid) authenticated++ }
        expendsList?.forEach { if (it.isValid) authenticated++ }

        return authenticated.div(authenticableItems.toDouble()) * 100
    }

    fun getLiquidValue(): BigDecimal {
        val freight = getListTotalValue(FREIGHT)
        val commission = freightsList?.sumOf { it.getCommissionValue() } ?: BigDecimal.ZERO
        val refuel = getListTotalValue(REFUEL)
        val expend = getListTotalValue(EXPEND)

        return freight.subtract(refuel).subtract(expend).subtract(commission)
    }

    fun getListSize(listTag: Int): Int {
        return when (listTag) {
            FREIGHT -> freightsList?.size ?: 0
            EXPEND -> expendsList?.size ?: 0
            REFUEL -> refuelsList?.size ?: 0
            AID -> aidList?.size ?: 0
            else -> 0
        }
    }

    fun getListTotalValue(listTag: Int): BigDecimal {
        return when (listTag) {
            FREIGHT -> freightsList?.map { it.value }?.sumOf { it } ?: BigDecimal.ZERO
            EXPEND -> expendsList?.map { it.value }?.sumOf { it } ?: BigDecimal.ZERO
            REFUEL -> refuelsList?.map { it.totalValue }?.sumOf { it } ?: BigDecimal.ZERO
            AID -> aidList?.map { it.value }?.sumOf { it } ?: BigDecimal.ZERO
            else -> BigDecimal.ZERO
        }
    }

    fun getListOfIdsForList(listTag: Int): List<String> {
        return when (listTag) {
            FREIGHT -> freightsList?.mapNotNull { it.id } ?: emptyList()
            EXPEND -> expendsList?.mapNotNull { it.id } ?: emptyList()
            REFUEL -> refuelsList?.mapNotNull { it.id } ?: emptyList()
            AID -> aidList?.mapNotNull { it.id } ?: emptyList()

            else -> emptyList()
        }
    }

    fun getDifferenceBetweenInitialAndFinalOdometerMeasure(): BigDecimal {
        return if (initialOdometerMeasurement != null && finalOdometerMeasurement != null)
            finalOdometerMeasurement!!.subtract(initialOdometerMeasurement)
        else BigDecimal.ZERO
    }

    fun isReadyToBeFinished(): Boolean {
        return getTravelAuthenticationPercent() == 100.0 && !freightsList.isNullOrEmpty()
    }

    fun isEmptyTravel(): Boolean {
        return getListSize(FREIGHT) + getListSize(EXPEND) + getListSize(REFUEL) + getListSize(AID) == 0
    }

    fun validateForSaving() {
        freightsList?.also {
            if (it.isEmpty()) throw EmptyDataException("Empty Freight List")
            it.forEach { f ->
                if (!f.isValid) throw InvalidParameterException("Invalid Freight")
            }
        } ?: throw NullPointerException("Null Freight List")

        refuelsList?.forEach { r ->
            if (!r.isValid) throw InvalidParameterException("Invalid Refuel")
        }

        expendsList?.forEach { e ->
            if (!e.isValid) throw InvalidParameterException("Invalid Expend")
        }

        if (!isDatesInCorrectlyOrder()) throw InvalidParameterException("Dates are in wrong order")

        if (thereIsDuplicatedItems()) throw InvalidParameterException("There is duplicated items")

        if (!isOdometerMeasuresInCorrectlyOrder()) throw InvalidParameterException("Odometer measures are in wrong order")

    }

    private fun isOdometerMeasuresInCorrectlyOrder(): Boolean {
        finalOdometerMeasurement?.let {
            if (initialOdometerMeasurement < finalOdometerMeasurement) return true
        }
        return false
    }

    private fun isDatesInCorrectlyOrder(): Boolean {
        if (initialDate != null && finalDate != null) {
            return initialDate.isBefore(finalDate)
        }

        return false
    }

    private fun thereIsDuplicatedItems(): Boolean {
        freightsList?.let { freights ->
            val uniqueIds = mutableSetOf<String>()

            freights.mapNotNull { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    return true
                } else {
                    uniqueIds.add(id)
                }
            }
        }

        refuelsList?.let { refuels ->
            val uniqueIds = mutableSetOf<String>()

            refuels.mapNotNull { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    return true
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        expendsList?.let { expends ->
            val uniqueIds = mutableSetOf<String>()

            expends.mapNotNull { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    return true
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        aidList?.let { aids ->
            val uniqueIds = mutableSetOf<String>()

            aids.mapNotNull { it.id }.forEach { id ->
                if (uniqueIds.contains(id)) {
                    return true
                } else {
                    uniqueIds.add(id)
                }
            }

        }

        return false
    }

    fun isDeletable(): Boolean {
        if (isFinished) return false
        if(id == null) return false
        freightsList?.let {
            it.forEach { f ->
                if (f.isValid) return false
                if (f.id == null) return false
            }
        }
        refuelsList?.let {
            it.forEach { r ->
                if (r.isValid) return false
                if (r.id == null) return false
            }
        }
        expendsList?.let {
            it.forEach { e ->
                if (e.isValid) return false
                if (e.id == null) return false
            }
        }
        aidList?.let {
            if (it.isNotEmpty()) return false
        }
        return true
    }

    companion object {
        const val FREIGHT = 0
        const val EXPEND = 1
        const val REFUEL = 2
        const val AID = 3
    }

}