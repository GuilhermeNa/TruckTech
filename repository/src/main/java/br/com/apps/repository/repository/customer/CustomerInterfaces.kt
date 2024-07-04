package br.com.apps.repository.repository.customer

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Customer
import br.com.apps.repository.util.Response

interface CustomerRepositoryInterface : CustomerReadInterface

interface CustomerReadInterface {

    /**
     * Suspended function to fetch a list of customers associated with a master UID asynchronously.
     *
     * @param masterUid The master UID to identify which customers to retrieve.
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Customer objects or an Error with an exception.
     */
    suspend fun getCustomerListByMasterUid(masterUid: String, flow: Boolean = false)
            : LiveData<Response<List<Customer>>>

    /**
     * Suspended function to fetch a customer by their ID asynchronously.
     *
     * @param id The ID of the customer to retrieve.
     * @param flow If true, indicates to use a continuous flow of data updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with the Customer object or an Error with an exception.
     */
    suspend fun getCustomerById(id: String, flow: Boolean = false): LiveData<Response<Customer>>

}