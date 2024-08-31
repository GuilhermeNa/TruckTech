package br.com.apps.trucktech.service

import androidx.lifecycle.asFlow
import br.com.apps.model.exceptions.null_objects.NullCustomerException
import br.com.apps.model.model.Customer
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.UNKNOWN_EXCEPTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first

class CustomerService(private val repository: CustomerRepository) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun close() = scope.cancel()

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    suspend fun fetchCustomersByMasterUid(uid: String): List<Customer> {
        val response =
            repository.fetchCustomerListByMasterUid(uid).asFlow().first()
        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullCustomerException(UNKNOWN_EXCEPTION)
        }
    }

}