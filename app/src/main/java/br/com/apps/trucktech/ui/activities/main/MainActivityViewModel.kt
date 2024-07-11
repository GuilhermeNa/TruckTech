package br.com.apps.trucktech.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.exceptions.UserNotFoundException
import br.com.apps.model.model.Fine
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.model.user.CommonUser
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.advance.AdvanceRepository
import br.com.apps.repository.repository.auth.AuthenticationRepository
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.repository.fine.FineRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.loan.LoanRepository
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.repository.travel_aid.TravelAidRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.extractResponse
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.usecase.FleetUseCase
import br.com.apps.usecase.usecase.TravelUseCase
import br.com.apps.usecase.usecase.UserUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainActivityViewModel(
    private val userUseCase: UserUseCase,
    private val truckUseCase: FleetUseCase,
    private val authRepository: AuthenticationRepository,
    private val advanceRepository: AdvanceRepository,
    private val loanRepository: LoanRepository,
    private val fineRepository: FineRepository,
    private val travelRepository: TravelRepository,
    private val refuelRepository: RefuelRepository,
    private val freightRepository: FreightRepository,
    private val expendRepository: ExpendRepository,
    private val aidRepository: TravelAidRepository,
    private val travelUseCase: TravelUseCase
) : ViewModel() {

    private val _cachedTravels = MutableLiveData<List<Travel>>()
    val cachedTravels get() = _cachedTravels

    private val _cachedFines = MutableLiveData<List<Fine>>()
    val cachedFines get() = _cachedFines

    private val _cachedAdvances = MutableLiveData<List<Advance>>()
    val cachedAdvances get() = _cachedAdvances

    private val _cachedLoans = MutableLiveData<List<Loan>>()
    val cachedLoans get() = _cachedLoans

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
        viewModelScope.launch {
            try {
                fetchLoggedUser()
                fetchData()
            } catch (e: Exception) {
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
    private suspend fun fetchLoggedUser() {
        val userId = authRepository.getCurrentUser()?.uid
            ?: throw UserNotFoundException()

        val user = fetchUser(userId)
        val truck = fetchTruck(user.employeeId)
        mergeLoggedUserData(user, truck)

    }

    private suspend fun fetchUser(userId: String): CommonUser {
        val response = userUseCase.getById(userId, EmployeeType.DRIVER).asFlow().first()
        return response as CommonUser
    }

    private suspend fun fetchTruck(driverId: String): Truck {
        val response = truckUseCase.fetchTruckByDriverId(driverId).asFlow().first()
        return when (response) {
            is Response.Error -> throw UserNotFoundException()
            is Response.Success -> response.data ?: throw UserNotFoundException()
        }
    }

    private fun mergeLoggedUserData(user: CommonUser, truck: Truck) {
        val parts = user.name.split(" ")
        val name = parts[0]
        val surname = if (parts.size > 1) " ${parts[1]}" else ""

        loggedUser =
            LoggedUser(
                masterUid = truck.masterUid,
                driverId = truck.driverId,
                truckId = truck.id!!,
                uid = user.uid,
                truck = truck,
                orderCode = user.orderCode,
                orderNumber = user.orderNumber,
                name = ("$name$surname"),
                email = user.email,
                averageAim = BigDecimal(truck.averageAim),
                performanceAim = BigDecimal(truck.performanceAim),
                urlImage = user.urlImage,
                permissionLevelType = user.permission,
                commissionPercentual = truck.commissionPercentual
            )

    }

    /**
     * Initialize listener to get common data for fragments in cache
     */
    private suspend fun fetchData() {
        coroutineScope {

            val driverId = loggedUser.driverId

            launch { fetchAdvances(driverId) }
            launch { fetchLoans(driverId) }
            launch { fetchFines(driverId) }
            fetchTravels(driverId)
        }
    }

    private suspend fun fetchAdvances(driverId: String) {
        advanceRepository.getAdvanceListByEmployeeIdAndPaymentStatus(
            employeeId = driverId, isPaid = false, flow = true
        ).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> {
                    _cachedAdvances.value = response.data
                        ?: throw NullPointerException()
                }
            }
        }
    }

    private suspend fun fetchLoans(driverId: String) {
        loanRepository.getLoanListByEmployeeIdAndPaymentStatus(
            employeeId = driverId, isPaid = false, flow = true
        ).asFlow().collect { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> {
                    _cachedLoans.value = response.data
                        ?: throw NullPointerException()
                }
            }
        }

    }

    private suspend fun fetchFines(driverId: String) {
        fineRepository.getFineListByDriverId(driverId, flow = true)
            .asFlow().collect { response ->
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> {
                        _cachedFines.value = response.data
                            ?: throw NullPointerException()
                    }
                }
            }
    }

    private suspend fun fetchTravels(driverId: String) {
        coroutineScope {

            val travelFLow = travelRepository.getTravelListByDriverId(driverId, flow = true)
                .asFlow()

            val freightFlow = freightRepository.getFreightListByDriverId(driverId, flow = true)
                .asFlow()

            val refuelFlow = refuelRepository.getRefuelListByDriverId(driverId, flow = true)
                .asFlow()

            val expendFlow = expendRepository.getExpendListByDriverId(driverId, flow = true)
                .asFlow()

            val aidFLow = aidRepository.getTravelAidListByDriverId(driverId, flow = true)
                .asFlow()

            val combinedFlow = combineFlows(
                travelFLow,
                refuelFlow,
                freightFlow,
                expendFlow,
                aidFLow
            ) { travelResp, refuelResp, freightResp, expendResp, aidResp ->
                val travels = travelResp.extractResponse()

                travelUseCase.mergeTravelData(
                    travelList = travels,
                    refuelList = refuelResp.extractResponse(),
                    freightList = freightResp.extractResponse(),
                    expendList = expendResp.extractResponse(),
                    aidList = aidResp.extractResponse()
                )

                travels
            }

            combinedFlow.collect { travels ->
                _cachedTravels.value = travels
                setState(State.Loaded)
            }

        }
    }

    private fun combineFlows(
        travelFlow: Flow<Response<List<Travel>>>,
        refuelFlow: Flow<Response<List<Refuel>>>,
        freightFlow: Flow<Response<List<Freight>>>,
        expendFlow: Flow<Response<List<Expend>>>,
        aidFLow: Flow<Response<List<TravelAid>>>,
        transform: (
            Response<List<Travel>>,
            Response<List<Refuel>>,
            Response<List<Freight>>,
            Response<List<Expend>>,
            Response<List<TravelAid>>
        ) -> List<Travel>
    ): Flow<List<Travel>> {
        return travelFlow
            .combine(refuelFlow) { tra, ref ->
                Pair(tra, ref)
            }
            .combine(freightFlow) { (tra, ref), fre -> Triple(tra, ref, fre) }
            .combine(expendFlow) { (tra, ref, fre), exp -> Quadruple(tra, ref, fre, exp) }
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

    fun getTravels(): List<Travel> {
        return cachedTravels.value ?: emptyList()
    }

    fun getFines(): List<Fine> {
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
    val orderCode: Int,
    val orderNumber: Int,
    val name: String,
    val email: String,
    val averageAim: BigDecimal,
    val performanceAim: BigDecimal,
    val urlImage: String? = null,
    val permissionLevelType: PermissionLevelType,
    val commissionPercentual: BigDecimal
)