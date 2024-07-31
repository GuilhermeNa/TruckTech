package br.com.apps.repository.repository

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.repository.bank.BankRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BankRepositoryTest {

    private val repository = mockk<BankRepository>()

    //---------------------------------------------------------------------------------------------//
    // fetchAdvanceListByEmployeeIdAndPaymentStatus()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the common list of banks registered`() = runTest {
        coEvery {
            repository.fetchBankList()
        }.returns(MutableLiveData())
    }


}
