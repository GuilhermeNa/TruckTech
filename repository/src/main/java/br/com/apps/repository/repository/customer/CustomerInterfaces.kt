package br.com.apps.repository.repository.customer

import androidx.lifecycle.LiveData
import br.com.apps.model.model.Customer
import br.com.apps.repository.util.Response

interface CustomerRepositoryI : CustomerReadI

interface CustomerReadI {

    suspend fun getCustomerListByMasterUid(masterUid: String, flow: Boolean = false)
            : LiveData<Response<List<Customer>>>

}