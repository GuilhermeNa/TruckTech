package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.customer.CustomerReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CustomerReadImplTest {

    private val repository = mockk<CustomerReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchAdvanceListByEmployeeIdAndPaymentStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the customer list by master uid`() = runTest {
        coEvery {
            repository.fetchCustomerListByMasterUid(
                uid = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

    @Test
    fun `should return the customer list by id`() = runTest {
        coEvery {
            repository.fetchCustomerById(
                id = "1",
                flow = true
            )
        }.returns(MutableLiveData())
    }

}