package br.com.apps.model.model.travel

import br.com.apps.model.exceptions.DateOrderException
import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.OdometerOrderException
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.InvalidParameterException
import java.time.LocalDateTime

data class Travel(
    val masterUid: String,
    val id: String? = null,
    val truckId: String,
    val driverId: String,

    @field:JvmField
    var isFinished: Boolean,
    var considerAverage: Boolean,

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
        return freightsList?.sumOf { it.getCommissionValue() } ?: BigDecimal.ZERO
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
            if (it.isEmpty()) throw EmptyDataException("Nenhuma viagem encontrada")
            it.forEach { f ->
                if (!f.isValid) throw InvalidParameterException("Frete não validado")
            }
        } ?: throw NullPointerException("Falha ao carregar fretes")

        refuelsList?.forEach { r ->
            if (!r.isValid) throw InvalidParameterException("Abastecimento não validado")
        }

        expendsList?.forEach { e ->
            if (!e.isValid) throw InvalidParameterException("Despesa não validada")
        }

        if (!isDatesInCorrectlyOrder()) throw DateOrderException("Datas em ordem incorreta")

        if (thereIsDuplicatedItems()) throw DuplicatedItemsException("Existem itens duplicados")

        if (!isOdometerMeasuresInCorrectlyOrder()) throw OdometerOrderException("Quilometragem incorreta")

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
        if (id == null) return false
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

    fun getProfitPercent(): BigDecimal {
        val profit = getListTotalValue(FREIGHT)
        val waste =
            getListTotalValue(REFUEL) +
                    getListTotalValue(EXPEND) +
                    getCommissionValue()

        return if (profit != BigDecimal.ZERO && waste != BigDecimal.ZERO)
            waste
                .divide(profit, 2, RoundingMode.HALF_EVEN)
                .subtract(BigDecimal(1.0))
                .multiply(BigDecimal(100.0))
                .abs()
        else BigDecimal.ZERO

    }

    fun getFuelAverage(): BigDecimal {
            return refuelsList?.let { refuels ->
            val liters = refuels.sumOf { it.amountLiters }
            val distance = getDifferenceBetweenInitialAndFinalOdometerMeasure()
            distance.divide(liters, 2, RoundingMode.HALF_EVEN)
        } ?: BigDecimal.ZERO
    }

    fun shouldConsiderAverage(): Boolean {
        return refuelsList?.last()?.isCompleteRefuel ?: false
    }

    companion object {
        const val FREIGHT = 0
        const val EXPEND = 1
        const val REFUEL = 2
        const val AID = 3
    }

}

data class PerformanceItem(
    val title: String,
    val meta: String,
    val hit: String,
    val percent: String,
    var progressBar: Int
)