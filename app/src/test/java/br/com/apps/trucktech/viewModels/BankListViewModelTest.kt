package br.com.apps.trucktech.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.enums.PixType
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.BankLFData
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.BankListFragmentViewModel
import br.com.apps.trucktech.util.state.State
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class BankListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel = mockk<BankListFragmentViewModel>(relaxed = true)
    private val observer: Observer<BankLFData> = mockk(relaxed = true)

    @Before
    fun setup() {
        every { viewModel.data } returns MutableLiveData()
        every { viewModel.state } returns MutableLiveData(State.Loading)
    }

    @After
    fun tearDown() = viewModel.data.removeObserver(observer)

    //---------------------------------------------------------------------------------------------//
    // data observer()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_call_adapter_update_after_first_loading() {
        fun dataSet() = BankLFData(
            bankList = listOf(
                Bank(
                    id = "1",
                    name = "Bradesco",
                    code = 123,
                    urlImage = "urlImage"
                ),
                Bank(
                    id = "2",
                    name = "Itau",
                    code = 456,
                    urlImage = "urlImage"
                ),
            ),
            bankAccList = listOf(
                BankAccount(
                    masterUid = "1",
                    id = "2",
                    employeeId = "3",
                    code = 123,
                    pixType = PixType.PHONE,
                    pix = "62154856575",
                    mainAccount = true,
                    bankName = "Bradesco",
                    accNumber = 1234567,
                    branch = 1234,
                    insertionDate = LocalDateTime.of(2024, 5, 1, 12, 0, 0)
                ),
                BankAccount(
                    masterUid = "1",
                    id = "3",
                    employeeId = "3",
                    code = 456,
                    pixType = PixType.CPF,
                    pix = "12345678980",
                    mainAccount = false,
                    bankName = "Itau",
                    accNumber = 1234567,
                    branch = 1234,
                    insertionDate = LocalDateTime.of(2024, 5, 1, 12, 0, 0)
                ),
            )
        )
        val observer = Observer<BankLFData> {
            assertEquals(2, it.bankAccList.size)
            viewModel.data.removeObserver(observer)
        }

        viewModel.data.observeForever(observer)
        viewModel.data.value = dataSet()
    }

}