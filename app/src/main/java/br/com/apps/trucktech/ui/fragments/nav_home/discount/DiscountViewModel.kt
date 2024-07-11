package br.com.apps.trucktech.ui.fragments.nav_home.discount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.repository.util.Response
import br.com.apps.usecase.usecase.TravelUseCase

class DiscountViewModel(private val travelUseCase: TravelUseCase) : ViewModel() {

    private val _state = MutableLiveData<DiscountFState>()
    val state get() = _state

    /**
     * LiveData holding the response data of type [Response] with a [Advance]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<DiscountFData>()
    val data get() = _data

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init { setState(DiscountFState.Loading) }

    fun setState(state: DiscountFState) {
        _state.value = state
    }

    fun initFragmentData(loans: List<Loan>, advances: List<Advance>, travels: List<Travel>)
            : DiscountFData {
        val travelAids = travelUseCase.getTravelAidListWitchIsNotRefundYet(travels)

        fun getState(): DiscountFState {
            return if (advances.isNotEmpty() || loans.isNotEmpty() || travelAids.isNotEmpty()) {
                DiscountFState.Loaded(
                    hasCostHelps = travelAids.isNotEmpty(),
                    hasAdvances = advances.isNotEmpty(),
                    hasLoans = loans.isNotEmpty()
                )
            } else {
                DiscountFState.Empty
            }
        }
        setState(getState())
        return DiscountFData(travelAids, advances, loans)
    }

}

data class DiscountFData(
    val costHelps: List<TravelAid>,
    val advances: List<Advance>,
    val loans: List<Loan>
)

