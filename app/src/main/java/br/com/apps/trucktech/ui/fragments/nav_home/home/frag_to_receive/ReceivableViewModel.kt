package br.com.apps.trucktech.ui.fragments.nav_home.home.frag_to_receive

import androidx.lifecycle.ViewModel
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Outlay
import java.math.BigDecimal
import java.math.RoundingMode

class ReceivableViewModel : ViewModel() {

    private var _isFirstBoot = true
    val isFirstBoot get() = _isFirstBoot

    fun setFirstBoot() { _isFirstBoot = false }

    fun processData(
        loans: List<Loan>?,
        advances: List<Advance>?,
        outlays: List<Outlay>?,
        freights: List<Freight>?
    ): ReceivableFData? {
        return if (freights != null && loans != null && advances != null && outlays != null) {
            fun getCommission(): BigDecimal = freights.sumOf { it.getCommissionValue() }
            fun getComSize(): Int = freights.size
            fun getOutlay(): BigDecimal = outlays.sumOf { it.value }
            fun getOutSize(): Int = outlays.size
            fun getDiscount(): BigDecimal = loans.sumOf { it.getNextInstalmentValue() } + advances.sumOf { it.value }
            fun getDisSize(): Int = loans.size + advances.size
            fun getGrossValue(): BigDecimal =  getCommission().add(getOutlay())

            fun getCommissionPerc(): Int {
                return if (getGrossValue() > BigDecimal.ZERO) {
                    getCommission()
                        .divide(getGrossValue(), 2, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal(100))
                        .toPlainString()
                        .substringBefore(".")
                        .toInt()

                } else {
                    0
                }
            }
            fun getOutlayPerc(): Int {
                return if (getGrossValue() > BigDecimal.ZERO) {
                    getOutlay()
                        .divide(getGrossValue(), 2, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal(100))
                        .toPlainString()
                        .substringBefore(".")
                        .toInt()
                } else {
                    0
                }
            }
            fun getDiscountPerc(): Int {
                return if (getGrossValue() > BigDecimal.ZERO) {
                    getDiscount()
                        .divide(getGrossValue(), 2, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal(100))
                        .toPlainString()
                        .substringBefore(".")
                        .toInt()
                } else {
                    0
                }
            }

            fun getCommissionAmount(): String {
                return getComSize().let {
                    if (it > 1) {
                        "$it fretes"
                    } else {
                        "$it frete"
                    }
                }
            }
            fun getOutlayAmount(): String {
                return getOutSize().let {
                    if (it > 1) {
                        "$it reembolsos"
                    } else {
                        "$it reembolso"
                    }
                }
            }
            fun getDiscountAmount(): String {
                return getDisSize().let {
                    if (it > 1) {
                        "$it descontos"
                    } else {
                        "$it descontos"
                    }
                }
            }

            val liquidValue = getCommission().subtract(getOutlay()).subtract(getDiscount())

            ReceivableFData(
               liquid = liquidValue,
                commissionPerc = getCommissionPerc(),
                outlayPerc = getOutlayPerc(),
                discountPerc = getDiscountPerc(),
                commissionSize = getCommissionAmount(),
                outlaySize = getOutlayAmount(),
                discountSize = getDiscountAmount()
            )
        } else null

    }
}

class ReceivableFData(
   val liquid: BigDecimal,

    val commissionPerc: Int,
    val outlayPerc: Int,
    val discountPerc: Int,

    val commissionSize: String,
    val outlaySize: String,
    val discountSize: String

)
