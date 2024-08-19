package br.com.apps.trucktech.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.exceptions.UserNotFoundException
import br.com.apps.model.exceptions.null_objects.NullAdvanceException
import br.com.apps.model.exceptions.null_objects.NullFineException
import br.com.apps.model.exceptions.null_objects.NullFreightException
import br.com.apps.model.exceptions.null_objects.NullLoanException
import br.com.apps.model.exceptions.null_objects.NullOutlayException
import br.com.apps.model.exceptions.null_objects.NullReceivableException
import br.com.apps.model.exceptions.null_objects.NullTransactionException
import br.com.apps.model.exceptions.null_objects.NullTravelAidException
import br.com.apps.model.exceptions.null_objects.NullUserException
import br.com.apps.model.model.FleetFine
import br.com.apps.model.model.User
import br.com.apps.model.model.finance.Transaction
import br.com.apps.model.model.finance.payable.EmployeePayable
import br.com.apps.model.model.finance.receivable.EmployeeReceivable
import br.com.apps.model.model.finance.receivable.EmployeeReceivable.Companion.merge
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Advance.Companion.merge
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.payroll.Loan.Companion.merge
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Freight.Companion.mergePayables
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.Outlay.Companion.mergePayables
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.travel.Travel.Companion.merge
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.repository.advance.AdvanceRepository
import br.com.apps.repository.repository.auth.AuthenticationRepository
import br.com.apps.repository.repository.fine.FineRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.loan.LoanRepository
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.repository.payable.EmployeePayableRepository
import br.com.apps.repository.repository.receivable.EmployeeReceivableRepository
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.transaction.TransactionRepository
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.repository.travel_aid.TravelAidRepository
import br.com.apps.repository.repository.user.UserRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.extractResponse
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.FleetUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainActivityViewModel(
    private val userRepo: UserRepository,
    private val truckUseCase: FleetUseCase,
    private val authRepository: AuthenticationRepository,
    private val advanceRepo: AdvanceRepository,
    private val loanRepo: LoanRepository,
    private val fineRepository: FineRepository,
    private val travelRepository: TravelRepository,
    private val refuelRepository: RefuelRepository,
    private val freightRepository: FreightRepository,
    private val outlayRepository: OutlayRepository,
    private val aidRepo: TravelAidRepository,
    private val receivableRepo: EmployeeReceivableRepository,
    private val payableRepo: EmployeePayableRepository,
    private val transactionRepo: TransactionRepository
) : ViewModel() {

    private val _cachedTravels = MutableLiveData<List<Travel>>()
    val cachedTravels get() = _cachedTravels

    private val _cachedFines = MutableLiveData<List<FleetFine>>()
    val cachedFines get() = _cachedFines

    private val _cachedAdvances = MutableLiveData<List<Advance>>()
    val cachedAdvances get() = _cachedAdvances

    private val _cachedLoans = MutableLiveData<List<Loan>>()
    val cachedLoans get() = _cachedLoans

    private val _cachedAids = MutableLiveData<List<TravelAid>>()
    val cachedAids get() = _cachedAids

    private val _cachedFreightToPay = MutableLiveData<List<Freight>>()
    val cachedFreightToPay get() = _cachedFreightToPay

    private val _cachedOutlayToPay = MutableLiveData<List<Outlay>>()
    val cachedOutlayToPay get() = _cachedOutlayToPay

    /**
     * LoggedUser
     */
    lateinit var loggedUser: LoggedUser

    /**
     * State
     */
    private val _state = MutableLiveData<State>()
    val state get() = _state

    /**
     * Components
     */
    private var _components: MutableLiveData<VisualComponents> = MutableLiveData()
    val components get() = _components

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {

                val employeeId = fetchLoggedUser()

                launch { fetchEmployeePayablesFlow(employeeId) }
                launch { fetchEmployeeReceivablesFlow(employeeId) }
                launch { fetchFinesFlow(employeeId) }
                fetchTravelsFlow(employeeId)

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))
            }
        }
    }

    /**
     * SetState
     */
    private fun setState(state: State) {
        if (_state.value != this@MainActivityViewModel.state) {
            _state.value = state
        }
    }

    /**
     * Define components view
     */
    fun setComponents(components: VisualComponents) {
        _components.value = components
    }

    /**
     * Load user data
     */
    private suspend fun fetchLoggedUser(): String {

        suspend fun fetchUser(userId: String): User {
            val response = userRepo.fetchById(userId)
                .asFlow().first()
            return when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data ?: throw NullUserException()
            }
        }

        suspend fun fetchTruck(driverId: String): Truck {
            val response = truckUseCase.fetchTruckByDriverId(driverId)
                .asFlow().first()
            return when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> response.data ?: throw UserNotFoundException()
            }
        }

        fun mergeLoggedUserData(user: User, truck: Truck) {
            val parts = user.name.split(" ")
            val name = parts[0]
            val surname = if (parts.size > 1) " ${parts[1]}" else ""

            loggedUser =
                LoggedUser(
                    masterUid = truck.masterUid,
                    driverId = truck.employeeId,
                    truckId = truck.id,
                    uid = user.uid,
                    truck = truck,
                    lastRequest = user.lastRequest,
                    name = ("$name$surname"),
                    email = user.email,
                    averageAim = BigDecimal(truck.averageAim),
                    performanceAim = BigDecimal(truck.performanceAim),
                    urlImage = user.urlImage,
                    accessLevel = user.permission,
                    commissionPercentual = truck.commissionPercentual
                )

        }

        val userId = authRepository.getCurrentUser()?.uid
            ?: throw UserNotFoundException()

        val user = fetchUser(userId)
        val truck = fetchTruck(user.employeeId)
        mergeLoggedUserData(user, truck)

        return user.employeeId
    }

    /**
     * Load receivables
     */
    private suspend fun fetchEmployeeReceivablesFlow(employeeId: String) {

        suspend fun fetchAndMergeTransactions(receivables: List<EmployeeReceivable>): List<Transaction> {
            val recIds = receivables.map { it.id }

            return if (recIds.isNotEmpty()) {
                val response = transactionRepo.fetchTransactionsByParentIds(recIds)
                    .asFlow().first()
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data?.also { receivables.merge(it) }
                        ?: throw NullTransactionException()
                }
            } else {
                emptyList()
            }
        }

        suspend fun fetchAndMergeLoans(receivables: List<EmployeeReceivable>): List<Loan> {
            val loanIds =
                receivables.filter { it.type == EmployeeReceivableTicket.LOAN }.map { it.id }

            return if (loanIds.isNotEmpty()) {
                val response = loanRepo.fetchLoanByIds(loanIds)
                    .asFlow().first()
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data?.also { it.merge(receivables) }
                        ?: throw NullLoanException()
                }

            } else {
                emptyList()

            }
        }

        suspend fun fetchAndMergeAdvances(receivables: List<EmployeeReceivable>): List<Advance> {
            val advanceIds =
                receivables.filter { it.type == EmployeeReceivableTicket.ADVANCE }.map { it.id }

            return if (advanceIds.isNotEmpty()) {
                val response = advanceRepo.fetchAdvanceByIds(advanceIds)
                    .asFlow().first()
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data?.also { it.merge(receivables) }
                        ?: throw NullAdvanceException()
                }

            } else {
                emptyList()

            }
        }

        suspend fun fetchAndMergeAids(receivables: List<EmployeeReceivable>): List<TravelAid> {
            val aidIds =
                receivables.filter { it.type == EmployeeReceivableTicket.TRAVEL_AID }.map { it.id }

            return if (receivables.isNotEmpty()) {
                val response = aidRepo.fetchTravelAidByIds(aidIds)
                    .asFlow().first()

                return when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data ?: throw NullTravelAidException()
                }

            } else {
                emptyList()
            }

        }
        receivableRepo.fetchReceivableByEmployeeIdAndStatus(
            id = employeeId, isReceived = false, flow = true
        ).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception

                is Response.Success -> response.data?.let { receivables ->
                    receivables as List<EmployeeReceivable>
                    receivables.merge(fetchAndMergeTransactions(receivables))
                    _cachedLoans.value = fetchAndMergeLoans(receivables)
                    _cachedAdvances.value = fetchAndMergeAdvances(receivables)
                    _cachedAids.value = fetchAndMergeAids(receivables)

                } ?: throw NullReceivableException()
            }
        }
    }

    /**
     * Load payables
     */
    private suspend fun fetchEmployeePayablesFlow(employeeId: String) {

        suspend fun fetchAndMergeOutlays(payables: List<EmployeePayable>): List<Outlay> {
            val outlaysIds =
                payables.filter { it.type == EmployeePayableTicket.OUTLAY }.map { it.id }

            return if (outlaysIds.isNotEmpty()) {
                val response = outlayRepository.fetchOutlayByIds(outlaysIds)
                    .asFlow().first()
                return when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data?.also { it.mergePayables(payables) }
                        ?: throw NullOutlayException()
                }
            } else {
                emptyList()

            }
        }

        suspend fun fetchAndMergeFreights(payables: List<EmployeePayable>): List<Freight> {
            val freightIds =
                payables.filter { it.type == EmployeePayableTicket.COMMISSION }.map { it.id }

            return if (freightIds.isNotEmpty()) {
                val response = freightRepository.fetchFreightByIds(freightIds)
                    .asFlow().first()
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data?.also { it.mergePayables(payables) }
                        ?: throw NullOutlayException()
                }

            } else {
                emptyList()

            }
        }

        payableRepo.fetchPayablesByEmployeeIdAndStatus(
            id = employeeId, isPaid = false, flow = true
        ).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> {
                    response.data?.let { payables ->
                        payables as List<EmployeePayable>
                        _cachedFreightToPay.value = fetchAndMergeFreights(payables)
                        _cachedOutlayToPay.value = fetchAndMergeOutlays(payables)
                    } ?: throw NullFreightException()
                }
            }
        }
    }

    /**
     * Load fines
     */
    private suspend fun fetchFinesFlow(driverId: String) {
        fineRepository.fetchFineListByDriverId(driverId, flow = true)
            .asFlow().collect { response ->
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> {
                        _cachedFines.value = response.data
                            ?: throw NullFineException()
                    }
                }
            }
    }

    /**
     * Load travels
     */
    private suspend fun fetchTravelsFlow(driverId: String) {
        val travelFLow = travelRepository.fetchTravelListByDriverId(driverId, flow = true)
            .asFlow()

        val freightFlow = freightRepository.fetchFreightListByDriverId(driverId, flow = true)
            .asFlow()

        val refuelFlow = refuelRepository.fetchRefuelListByDriverId(driverId, flow = true)
            .asFlow()

        val expendFlow = outlayRepository.fetchOutlayListByDriverId(driverId, flow = true)
            .asFlow()

        val aidFLow = aidRepo.fetchTravelAidListByDriverId(driverId, flow = true)
            .asFlow()

        val combinedFlow = combineFlows(
            travelFLow, refuelFlow, freightFlow, expendFlow, aidFLow
        ) { travelResp, refuelResp, freightResp, expendResp, aidResp ->
            val travels = travelResp.extractResponse()
            val refuels = when (refuelResp) {
                is Response.Error -> emptyList()
                is Response.Success -> refuelResp.data ?: throw NullPointerException()
            }
            val freights = when (freightResp) {
                is Response.Error -> emptyList()
                is Response.Success -> freightResp.data ?: throw NullPointerException()
            }
            val expends = when (expendResp) {
                is Response.Error -> emptyList()
                is Response.Success -> expendResp.data ?: throw NullPointerException()
            }
            val aids = when (aidResp) {
                is Response.Error -> emptyList()
                is Response.Success -> aidResp.data ?: throw NullPointerException()
            }

            travels.merge(
                nRefuels = refuels, nFreights = freights,
                nOutlays = expends, nAids = aids
            )

            travels

        }

        combinedFlow.collect { travels ->
            _cachedTravels.value = travels
            setState(State.Loaded)
        }

    }

    private fun combineFlows(
        travelFlow: Flow<Response<List<Travel>>>,
        refuelFlow: Flow<Response<List<Refuel>>>,
        freightFlow: Flow<Response<List<Freight>>>,
        outlayFlow: Flow<Response<List<Outlay>>>,
        aidFLow: Flow<Response<List<TravelAid>>>,
        transform: (
            Response<List<Travel>>,
            Response<List<Refuel>>,
            Response<List<Freight>>,
            Response<List<Outlay>>,
            Response<List<TravelAid>>
        ) -> List<Travel>
    ): Flow<List<Travel>> {
        return travelFlow
            .combine(refuelFlow) { tra, ref -> Pair(tra, ref) }
            .combine(freightFlow) { (tra, ref), fre -> Triple(tra, ref, fre) }
            .combine(outlayFlow) { (tra, ref, fre), exp -> Quadruple(tra, ref, fre, exp) }
            .combine(aidFLow) { (tra, ref, fre, exp), aid ->
                transform(tra, ref, fre, exp, aid)
            }
    }


    fun getLoans(): List<Loan> {
        return cachedLoans.value ?: emptyList()
    }

    fun getAdvances(): List<Advance> {
        return cachedAdvances.value ?: emptyList()
    }

    fun getOutlays(): List<Outlay> {
        return cachedOutlayToPay.value ?: emptyList()
    }

    fun getFreights(): List<Freight> {
        return cachedFreightToPay.value ?: emptyList()
    }

    fun getTravels(): List<Travel> {
        return cachedTravels.value ?: emptyList()
    }

    fun getFines(): List<FleetFine> {
        return cachedFines.value ?: emptyList()
    }

}

private data class Quadruple<T1, T2, T3, T4>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4
)

class VisualComponents(val hasBottomNavigation: Boolean = false)

data class LoggedUser(
    val masterUid: String,
    val uid: String,
    val driverId: String,
    val truckId: String,

    val truck: Truck,
    val lastRequest: Long,
    val name: String,
    val email: String,
    val averageAim: BigDecimal,
    val performanceAim: BigDecimal,
    val urlImage: String? = null,
    val accessLevel: AccessLevel,
    val commissionPercentual: BigDecimal
)