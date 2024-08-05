package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.exceptions.null_objects.NullCustomerException
import br.com.apps.model.exceptions.null_objects.NullFreightException
import br.com.apps.model.model.Customer
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Freight.Companion.merge
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FreightsListViewModel(
    private val vmData: FreightLVmData,
    private val fRepository: FreightRepository,
    private val cRepository: CustomerRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a list of expenditures [Freight]
     * to be displayed on screen.
     */
    private val _data = MutableLiveData<List<Freight>>()
    val data get() = _data

    private val _state = MutableLiveData<State>()
    val state get() = _state

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
                val cus = loadCustomers()
                loadFreights { fre ->
                    sendResponse(fre, cus)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                setState(State.Error(e))

            }
        }
    }

    private suspend fun loadCustomers(): List<Customer> {
        val response =
            cRepository.fetchCustomerListByMasterUid(vmData.masterUid).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullCustomerException(UNKNOWN_EXCEPTION)
        }
    }

    private suspend fun loadFreights(complete: (freights: List<Freight>) -> Unit) {
        fRepository.fetchFreightListByTravelId(vmData.travelId, true).asFlow()
            .collect { response ->
                when (response) {
                    is Response.Error -> throw response.exception
                    is Response.Success -> response.data?.let { complete(it) }
                        ?: throw NullFreightException(UNKNOWN_EXCEPTION)
                }
            }
    }

    private fun sendResponse(freights: List<Freight>, customers: List<Customer>) {
        if (freights.isEmpty()) setState(State.Empty)
        else {
            freights.merge(customers)
            setState(State.Loaded)
            setFreightData(freights)
        }
    }

    private fun setState(state: State) {
        if (state != _state.value) _state.value = state
    }

    private fun setFreightData(freights: List<Freight>) {
        _data.value = freights
    }

}

class FreightLVmData(
    val masterUid: String,
    val travelId: String
)