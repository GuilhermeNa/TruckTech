package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.advance.AdvanceReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AdvanceReadImplTest {

    private val repository = mockk<AdvanceReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchAdvanceListByEmployeeIdAndPaymentStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return an advance list when search for employee id and payment status`() = runTest {
        coEvery {
            repository.fetchAdvanceListByEmployeeIdAndPaymentStatus(
                id = "1",
                isPaid = true,
                flow = false
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // getAdvanceListByEmployeeIdsAndPaymentStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return an advance list when search for employee id list and payment status`() = runTest {
        coEvery {
            repository.fetchAdvanceListByEmployeeIdsAndPaymentStatus(
                ids = listOf("1", "2"),
                isPaid = true,
                flow = false
            )
        }.returns(MutableLiveData())
    }

}