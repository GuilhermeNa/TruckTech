package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.model.Customer
import br.com.apps.model.model.travel.Freight
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FreightsListViewModel(
    private val idHolder: IdHolder,
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
        loadData { customers, freights ->
            processData(customers, freights)
            sendResponse(freights)
        }
    }

    private fun loadData(complete: (customers: List<Customer>, freights: List<Freight>) -> Unit) {
        _state.value = State.Loading

        viewModelScope.launch {
            val customersResult = loadCustomers()
            val customers = customersResult.await()

            loadFreights { freights ->
                complete(customers, freights)
            }
        }
    }

    private suspend fun loadCustomers(): CompletableDeferred<List<Customer>> {
        val deferred = CompletableDeferred<List<Customer>>()

        cRepository.fetchCustomerListByMasterUid(idHolder.masterUid!!).asFlow().first { response ->
            when (response) {
                is Response.Error -> {
                    _state.value = State.Error(response.exception)
                    deferred.completeExceptionally(response.exception)
                }

                is Response.Success -> response.data?.let { deferred.complete(it) }
                    ?: deferred.completeExceptionally(NullPointerException(EMPTY_DATASET))

            }
            true
        }

        return deferred
    }

    private suspend fun loadFreights(complete: (freights: List<Freight>) -> Unit) {
        fRepository.fetchFreightListByTravelId(idHolder.travelId!!, true).asFlow()
            .collect { response ->
                when (response) {
                    is Response.Error -> _state.value = State.Error(response.exception)
                    is Response.Success -> response.data?.let { complete(it) }
                }
            }
    }

    private fun processData(customers: List<Customer>, freights: List<Freight>) {
        freights.forEach { f ->
            f.customer = customers.first { it.id == f.customerId }
        }
    }

    private fun sendResponse(freights: List<Freight>) {
        if(freights.isEmpty()) {
            _state.value = State.Empty
        } else {
            _state.value = State.Loaded
            _data.value = freights
        }
    }

}