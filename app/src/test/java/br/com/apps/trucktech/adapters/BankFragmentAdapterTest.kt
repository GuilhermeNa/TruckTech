package br.com.apps.trucktech.adapters

import android.content.Context
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.enums.PixType
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.BankLFData
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.private_adapters.BankFragmentAdapter
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class BankFragmentAdapterTest {

    private val mockkContext: Context = mockk()

    private lateinit var dataSet: BankLFData

    private lateinit var spy: BankFragmentAdapter

    @Before
    fun setup() {
        dataSet = BankLFData(
            bankList = listOf(
                Bank(
                    id = "1",
                    name = "Bradesco",
                    code = 123,
                    urlImage = "urlImage"
                ),
                Bank(
                    id = "1",
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

        spy = spyk(
            BankFragmentAdapter(
                context = mockkContext,
                _clickedPos = -1,
                data = BankLFData(emptyList(), emptyList()),
                clickListener = { s, i -> },
                defineNewMainAccount = {}
            )
        )
    }

    //---------------------------------------------------------------------------------------------//
    // update()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun should_update_adapter_when_receive_data() {
        every { spy.notifyDataSetChanged() } just runs // Não iremos chamar literalmente o notifyDataSetChanged
        spy.update(dataSet)
        val adapterSize = spy.itemCount

        verify { spy.notifyChange() } // Verifica se o metodo notify change foi invocado
        assertEquals(2, adapterSize)

        assertEquals("Bradesco", spy.data[0].bankName)
        assertEquals("Itau", spy.data[1].bankName)
    }

}