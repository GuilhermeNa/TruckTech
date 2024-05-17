package br.com.apps.usecase

import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.AdvanceRepository
import br.com.apps.repository.repository.ExpendRepository
import br.com.apps.repository.repository.FreightRepository
import br.com.apps.repository.repository.LoanRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PaymentCalculatorUseCase(
    private val dataSet: List<String>,
    private val freightRepository: FreightRepository,
    private val expendRepository: ExpendRepository,
    private val advanceRepository: AdvanceRepository,
    private val loanRepository: LoanRepository
) {

    private val mediator = MediatorLiveData<Unit>()
    private var employeeIdData = dataSet.toMutableList()
    private lateinit var freightData: List<Freight>
    private lateinit var expendData: List<Expend>
    private lateinit var advanceData: List<Advance>
    private lateinit var loanData: List<Loan>

    /**
     * Try to load data related to the given driver ID.
     *  1. Initiates loading of [Freight] data with comission to pay.
     *  2. Initiates loading of [Expend] data.
     *  3. Initiates loading of [Advance] data.
     *  4. Initiates loading of [Loan] data.
     *
     *  Once all data is loaded successfully, notifies to [_dataIsReady].
     *
     * @param driverId The ID of the driver for whom the data needs to be loaded.
     */
    fun loadDataForDrivers() {
        CoroutineScope(Dispatchers.Main).launch {
            val deferredA = CompletableDeferred<Unit>()
            val deferredB = CompletableDeferred<Unit>()
            val deferredC = CompletableDeferred<Unit>()
            val deferredD = CompletableDeferred<Unit>()

            launch { loadFreightDataWithCommissionToPay(deferredA) }
            launch { loadExpendDataForRefund(deferredB) }
            launch { loadAdvanceDataForDiscount(deferredC) }
            launch { loadLoanDataForDiscount(deferredD) }

            awaitAll(deferredA, deferredB, deferredC, deferredD)


        }
    }

    private suspend fun loadFreightDataWithCommissionToPay(deferred: CompletableDeferred<Unit>) {
        val liveData = freightRepository.getFreightListByDriverIdAndPaymentStatus(
            employeeIdData,
            isPaid = false
        )
        mediator.addSource(liveData) { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> {
                    response.data?.let {
                        freightData = it
                        deferred.complete(Unit)
                    }
                }
            }
            mediator.removeSource(liveData)
        }
    }

    private suspend fun loadExpendDataForRefund(deferred: CompletableDeferred<Unit>) {
        val liveData = expendRepository.getExpendListByDriverIdAndRefundableStatus(
            employeeIdData,
            paidByEmployee = true,
            alreadyRefunded = false
        )
        mediator.addSource(liveData) { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> {
                    response.data?.let {
                        expendData = it
                        deferred.complete(Unit)
                    }
                }
            }
            mediator.removeSource(liveData)
        }
    }

    private suspend fun loadAdvanceDataForDiscount(deferred: CompletableDeferred<Unit>) {
        val liveData = advanceRepository.getAdvanceListByEmployeeIdAndPaymentStatus(
            employeeIdData,
            isPaid = false
        )
        mediator.addSource(liveData) { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> {
                    response.data?.let {
                        advanceData = it
                        deferred.complete(Unit)
                    }
                }
            }
            mediator.removeSource(liveData)
        }
    }

    private suspend fun loadLoanDataForDiscount(deferred: CompletableDeferred<Unit>) {
        val liveData = loanRepository.getLoanListByEmployeeIdAndPaymentStatus(
            employeeIdData,
            isPaid = false
        )
        mediator.addSource(liveData) { response ->
            when (response) {
                is Response.Error -> {}
                is Response.Success -> {
                    response.data?.let {
                        loanData = it
                        deferred.complete(Unit)
                    }
                }
            }
            mediator.removeSource(liveData)
        }
    }



    /**
     * Update dataSet
     */
    fun updateEmployeeIdList(employeeIdData: List<String>) {
        this.employeeIdData.clear()
        this.employeeIdData.addAll(employeeIdData)
    }


}
