package br.com.apps.trucktech.ui.fragments.nav_home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.Fine
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.advance.AdvanceRepository
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.repository.fine.FineRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.loan.LoanRepository
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.ui.activities.main.LoggedUser
import br.com.apps.trucktech.util.state.State
import br.com.apps.usecase.TravelUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val TRAVEL = "Viagem"
private const val MONTH = "Mês"
private const val YEAR = "Ano"

class HomeViewModel(
    private val travelRepository: TravelRepository,
    private val freightRepository: FreightRepository,
    private val expendRepository: ExpendRepository,
    private val advanceRepository: AdvanceRepository,
    private val loanRepository: LoanRepository,
    private val refuelRepository: RefuelRepository,
    private val fineRepository: FineRepository,
    private val travelUseCase: TravelUseCase,
) : ViewModel() {

    private val _fragmentState = MutableLiveData<State>()
    val fragmentState get() = _fragmentState

    private val _data = MutableLiveData<HomeFData>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        setState(State.Loading)
    }

    fun setState(newState: State) {
        _fragmentState.postValue(newState)
    }

    fun loadData(loggedUser: LoggedUser) {
        val driverId = loggedUser.driverId

        viewModelScope.launch {
            try {

                //TODO toReceive data
                val freights = fetchFreights(driverId)
                val expends = fetchExpends(driverId)
                val advances = fetchAdvances(driverId)
                val loans = fetchLoans(driverId)

                //TODO performance
                val travels = fetchTravels(driverId)
                val refuels = fetchRefuels(driverId)

                //TODO fines
                val fines = fetchFines(driverId)

                travelUseCase.mergeTravelData(travels, freights, expends, refuels)

                setState(State.Loaded)

                _data.postValue(
                    HomeFData(
                        travels = travels,
                        advances = advances,
                        fines = fines,
                        loans = loans
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))
            }
        }
    }

    //TODO fines
    private suspend fun fetchFines(driverId: String): List<Fine> {
        val response = fineRepository.getFineListByDriverId(driverId).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

    //TODO performance
    private suspend fun fetchTravels(driverId: String): List<Travel> {
        val response = travelRepository.getTravelListByDriverId(driverId).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

    private suspend fun fetchRefuels(driverId: String): List<Refuel> {
        val response = refuelRepository.getRefuelListByDriverId(driverId).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

    //TODO toReceive
    private suspend fun fetchFreights(driverId: String): List<Freight> {
        val response =
            freightRepository.getFreightListByDriverIdAndPaymentStatus(
                driverId, isPaid = false
            ).asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

    private suspend fun fetchExpends(driverId: String): List<Expend> {
        val response =
            expendRepository.getExpendListByDriverIdAndRefundableStatus(
                driverId = driverId, paidByEmployee = true, alreadyRefunded = false
            ).asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

    private suspend fun fetchAdvances(driverId: String): List<Advance> {
        val response =
            advanceRepository.getAdvanceListByEmployeeIdAndPaymentStatus(
                employeeId = driverId, isPaid = false
            ).asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }

    }

    private suspend fun fetchLoans(driverId: String): List<Loan> {
        val response =
            loanRepository.getLoanListByEmployeeIdAndPaymentStatus(
                employeeId = driverId, isPaid = false
            ).asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException()
        }
    }

}

data class HomeFData(
    val travels: List<Travel>? = null,
    val advances: List<Advance>? = null,
    val fines: List<Fine>? = null,
    val loans: List<Loan>? = null
)





