package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.loan.LoanReadImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoanReadImplTest {

    private val repository = mockk<LoanReadImpl>()

    //---------------------------------------------------------------------------------------------//
    // fetchLoanListByEmployeeIdAndPaymentStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return loan list by employee ID and payment status`() = runTest {
        coEvery {
            repository.fetchLoanListByEmployeeIdAndPaymentStatus(
                employeeId = "1",
                isPaid = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }

    //---------------------------------------------------------------------------------------------//
    // fetchLoanListByEmployeeIdsAndPaymentStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return loan list by employee IDs and payment status`() = runTest {
        coEvery {
            repository.fetchLoanListByEmployeeIdsAndPaymentStatus(
                employeeIdList = listOf("1", "2"),
                isPaid = true,
                flow = true
            )
        }.returns(MutableLiveData())
    }
}