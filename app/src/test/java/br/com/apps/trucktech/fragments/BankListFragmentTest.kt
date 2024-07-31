package br.com.apps.trucktech.fragments

import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.BankListFragment
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.private_adapters.BankFragmentAdapter
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Test

class BankListFragmentTest {

    private val fragment: BankListFragment = spyk()
    private val adapter: BankFragmentAdapter = mockk()


    @Before
    fun setup() {

    }

    //---------------------------------------------------------------------------------------------//
    // data observer()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should update adapter after receive data`() {
   /*     fun getDataSet() = BankLFData(
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

        val data = getDataSet()

        every { fragment.updateFragmentData(data) } just runs
        fragment.updateFragmentData(data)

        verify { adapter.update(data) }*/
    }

}