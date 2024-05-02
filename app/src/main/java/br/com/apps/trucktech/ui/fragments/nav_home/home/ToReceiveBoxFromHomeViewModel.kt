package br.com.apps.trucktech.ui.fragments.nav_home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.Response
import br.com.apps.repository.repository.AdvanceRepository
import br.com.apps.repository.repository.ExpendRepository
import br.com.apps.repository.repository.FreightRepository
import br.com.apps.repository.repository.LoanRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class ToReceiveBoxFromHomeViewModel(
    private val freightRepository: FreightRepository,
    private val expendRepository: ExpendRepository,
    private val advanceRepository: AdvanceRepository,
    private val loanRepository: LoanRepository
) : ViewModel() {

    /**
     * LiveData that notifies when all data is ready and loaded.
     */
    private val _paymentData = MutableLiveData<DriverPayment>()
    val paymentData get() = _paymentData

    /**
     * The lists below ([freightData],  [expendData], [advanceData], [loanData]) are related
     * to the driver's logged in data and which will be considered for payment.
     */
    private lateinit var freightData: List<Freight>
    private lateinit var expendData: List<Expend>
    private lateinit var advanceData: List<Advance>
    private lateinit var loanData: List<Loan>

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    /**
     * Try to load data related to the given driver ID.
     *  1. Initiates loading of [Freight] data.
     *  2. Initiates loading of [Expend] data.
     *  3. Initiates loading of [Advance] data.
     *  4. Initiates loading of [Loan] data.
     *
     *  Once all data is loaded successfully, calculate values and set value to [_paymentData].
     *
     * @param driverId The ID of the driver for whom the data needs to be loaded.
     */
    fun loadData(driverId: String) {
        viewModelScope.launch {

            val deferredA = CompletableDeferred<Unit>()
            val deferredB = CompletableDeferred<Unit>()
            val deferredC = CompletableDeferred<Unit>()
            val deferredD = CompletableDeferred<Unit>()

            launch { loadFreightData(driverId, deferredA) }
            launch { loadExpendData(driverId, deferredB) }
            launch { loadAdvanceData(driverId, deferredC) }
            launch { loadLoanData(driverId, deferredD) }

            awaitAll(deferredA, deferredB, deferredC, deferredD)

            paymentData.value = DriverPayment(
                freightCommission = calculateFreightCommission(),
                freightAmount = freightData.size,
                expendValue = calculateExpendValue(),
                expendAmount = expendData.size,
                discountValue = calculateDiscountValue(),
                discountAmount = advanceData.size + loanData.size
            )
        }
    }

    private suspend fun loadFreightData(driverId: String, deferred: CompletableDeferred<Unit>) {
        val liveData = freightRepository.getFreightListByDriverIdAndPaymentStatus(
            driverId = driverId,
            isPaid = false,
            withFlow = false
        )
        liveData.asFlow().first().let { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> {
                    response.data?.let {
                        freightData = it
                        deferred.complete(Unit)
                    }
                }
            }
        }
    }

    private suspend fun loadExpendData(driverId: String, deferred: CompletableDeferred<Unit>) {
        val liveData = expendRepository.getExpendListByDriverIdAndRefundableStatus(
            driverId = driverId,
            paidByEmployee = true,
            alreadyRefunded = false,
            withFlow = false
        )
        liveData.asFlow().first().let { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> {
                    response.data?.let {
                        expendData = it
                        deferred.complete(Unit)
                    }
                }
            }
        }
    }

    private suspend fun loadAdvanceData(driverId: String, deferred: CompletableDeferred<Unit>) {
        val liveData = advanceRepository.getAdvanceListByEmployeeIdAndPaymentStatus(
            employeeId = driverId,
            isPaid = false,
            withFlow = false
        )
        liveData.asFlow().first().let { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> {
                    response.data?.let {
                        advanceData = it
                        deferred.complete(Unit)
                    }
                }
            }
        }
    }

    private suspend fun loadLoanData(driverId: String, deferred: CompletableDeferred<Unit>) {
        val liveData = loanRepository.getLoanListByEmployeeIdAndPaymentStatus(
            employeeId = driverId,
            isPaid = false,
            withFlow = false
        )
        liveData.asFlow().first().let { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> {
                    response.data?.let {
                        loanData = it
                        deferred.complete(Unit)
                    }
                }
            }
        }
    }

    private fun calculateFreightCommission(): BigDecimal {
        return freightData.sumOf { it.getCommissionValue() }
    }

    private fun calculateExpendValue(): BigDecimal {
        return expendData.sumOf { it.value!! }
    }

    private fun calculateDiscountValue(): BigDecimal {
        val advanceValue = advanceData.sumOf { it.value!! }
        val loanValue = loanData.sumOf { it.getInstallmentValue() }
        return advanceValue.plus(loanValue)
    }

    fun toFloat(commissionPercent: Int): Float {
        return BigDecimal(commissionPercent)
            .divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN)
            .toFloat()
    }

}

class DriverPayment(
    val freightCommission: BigDecimal,
    val freightAmount: Int,

    val expendValue: BigDecimal,
    val expendAmount: Int,

    val discountValue: BigDecimal,
    val discountAmount: Int,

    ) {

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
        return freightCommission
            .divide(calculateGrossReceivable(), 2, RoundingMode.HALF_EVEN)
            .multiply(BigDecimal(100))
            .toPlainString()
            .substringBefore(".")
            .toInt()
    }

    fun calculateExpendPercent(): Int {
        return expendValue
            .divide(calculateGrossReceivable(), 2, RoundingMode.HALF_EVEN)
            .multiply(BigDecimal(100))
            .toPlainString()
            .substringBefore(".")
            .toInt()
    }

    fun calculateDiscountPercent(): Int {
        return discountValue
            .divide(calculateGrossReceivable(), 2, RoundingMode.HALF_EVEN)
            .multiply(BigDecimal(100))
            .toPlainString()
            .substringBefore(".")
            .toInt()
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