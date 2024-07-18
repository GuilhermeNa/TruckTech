package br.com.apps.model.dto.payroll

import br.com.apps.model.exceptions.CorruptedFileException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Date

class LoanDtoTest {

    private lateinit var loanDto: LoanDto

    @Before
    fun setup() {
        loanDto = LoanDto(
            masterUid = "1",
            id = "2",
            employeeId = "3",
            date = Date(),
            value = 1000.0,
            installments = 2,
            installmentsAlreadyPaid = 0,
            isPaid = false
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        loanDto.masterUid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            loanDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        loanDto.id = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            loanDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when employeeId is null`() {
        loanDto.employeeId = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            loanDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        loanDto.date = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            loanDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        loanDto.value = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            loanDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when installments is null`() {
        loanDto.installments = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            loanDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when installmentsAlreadyPaid is null`() {
        loanDto.installmentsAlreadyPaid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            loanDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isPaid is null`() {
        loanDto.isPaid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            loanDto.validateDataIntegrity()
        }
    }

}