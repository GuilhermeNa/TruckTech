package br.com.apps.model.dto

import br.com.apps.model.dto.bank.BankAccountDto
import br.com.apps.model.exceptions.CorruptedFileException
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class BankAccountDtoTest {

    private lateinit var bankAcc: BankAccountDto

    @Before
    fun setup() {
        bankAcc = BankAccountDto(
            masterUid = "1",
            id = "2",
            employeeId = "3",
            bankId = "4",
            insertionDate = Date(),
            branch = 123,
            accNumber = 321,
            pix = "111.111.111-11",
            mainAccount = true,
            pixType = "CNPJ",
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        bankAcc.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            bankAcc.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when employeeId is null`() {
        bankAcc.employeeId = null
        assertThrows(CorruptedFileException::class.java) {
            bankAcc.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when insertionDate is null`() {
        bankAcc.insertionDate = null
        assertThrows(CorruptedFileException::class.java) {
            bankAcc.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when bankId is null`() {
        bankAcc.bankId = null
        assertThrows(CorruptedFileException::class.java) {
            bankAcc.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when branch is null`() {
        bankAcc.branch = null
        assertThrows(CorruptedFileException::class.java) {
            bankAcc.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when accNumber is null`() {
        bankAcc.accNumber = null
        assertThrows(CorruptedFileException::class.java) {
            bankAcc.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when mainAccount is null`() {
        bankAcc.mainAccount = null
        assertThrows(CorruptedFileException::class.java) {
            bankAcc.validateDataIntegrity()
        }
    }

}