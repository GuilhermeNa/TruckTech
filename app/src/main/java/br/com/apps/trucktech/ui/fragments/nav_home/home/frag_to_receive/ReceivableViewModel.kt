package br.com.apps.trucktech.ui.fragments.nav_home.home.frag_to_receive

import androidx.lifecycle.ViewModel
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Travel
import java.math.BigDecimal
import java.math.RoundingMode

class ReceivableViewModel : ViewModel() {

    private var _isFirstBoot = true
    val isFirstBoot get() = _isFirstBoot

    private fun setFirstBoot() {
        _isFirstBoot = false
    }

    fun initFragmentData(travels: List<Travel>?, loans: List<Loan>?, advances: List<Advance>?)
            : ReceivableFData? {

        setFirstBoot()

        return if (travels != null && loans != null && advances != null) {

            ReceivableFData(travels.filter { it.isFinished }, advances, loans)
        } else null

    }

    fun updateData(travels: List<Travel>, advances: List<Advance>, loans: List<Loan>) =
        ReceivableFData(travels.filter { it.isFinished }, advances, loans)

    fun toFloat(commissionPercent: Int): Float {
        return BigDecimal(commissionPercent)
            .divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN)
            .toFloat()
    }

}

data class ReceivableFData(
    private val travels: List<Travel>,
    private val advances: List<Advance>,
    private val loans: List<Loan>
) {

    private val freightCommission = calculateFreightCommission()
    private val freightAmount = travels.size

    private val expendValue = calculateExpendValue()
    private val expendAmount = travels.size

    private val discountValue = calculateDiscountValue()
    private val discountAmount = advances.size + loans.size

    private fun calculateFreightCommission(): BigDecimal {
        return travels
            .mapNotNull { it.freights }
            .flatten()
            .filter { !it.isCommissionPaid }
            .sumOf { it.getCommissionValue() }
    }

    private fun calculateExpendValue(): BigDecimal {
        return travels
            .mapNotNull { it.outlays }
            .flatten()
            .filter { it.isPaidByEmployee && !it.isAlreadyRefunded }
            .sumOf { it.value }
    }

    private fun calculateDiscountValue(): BigDecimal {
        val advanceValue = advances.sumOf { it.value }
        val loanValue = loans.sumOf { it.getInstallmentValue() }
        return advanceValue.plus(loanValue)
    }

    fun calculateLiquidReceivable(): BigDecimal {
        return freightCommission
            .add(expendValue)
            .subtract(discountValue)
    }

    private fun calculateGrossReceivable(): BigDecimal {
        return freightCommission
            .add(expendValue)
    }

    fun calculateCommissionPercent(): Int {
        return if (calculateGrossReceivable() > BigDecimal.ZERO) {
            freightCommission
                .divide(calculateGrossReceivable(), 2, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal(100))
                .toPlainString()
                .substringBefore(".")
                .toInt()

        } else {
            0
        }
    }

    fun calculateExpendPercent(): Int {
        return if (calculateGrossReceivable() > BigDecimal.ZERO) {
            expendValue
                .divide(calculateGrossReceivable(), 2, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal(100))
                .toPlainString()
                .substringBefore(".")
                .toInt()
        } else {
            0
        }
    }

    fun calculateDiscountPercent(): Int {
        return if (calculateGrossReceivable() > BigDecimal.ZERO) {
            discountValue
                .divide(calculateGrossReceivable(), 2, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal(100))
                .toPlainString()
                .substringBefore(".")
                .toInt()
        } else {
            0
        }
    }

    fun getFreightAmount(): String {
        return freightAmount.let {
            if (it > 1) {
                "$it fretes"
            } else {
                "$it frete"
            }
        }
    }

    fun getExpendAmount(): String {
        return expendAmount.let {
            if (it > 1) {
                "$it reembolsos"
            } else {
                "$it reembolso"
            }
        }
    }

    fun getDiscountAmount(): String {
        return discountAmount.let {
            if (it > 1) {
                "$it descontos"
            } else {
                "$it descontos"
            }
        }
    }

}