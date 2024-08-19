package br.com.apps.model.dto.payroll

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.test_cases.sampleLoan
import br.com.apps.model.test_cases.sampleLoanDto
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LoanDtoTest {

    private lateinit var loanDto: LoanDto

    @Before
    fun setup() {
        loanDto = sampleLoanDto()
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

    //---------------------------------------------------------------------------------------------//
    // validateDataForDbInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null`() {
        loanDto.masterUid = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            loanDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when employeeId is null`() {
        loanDto.employeeId = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            loanDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when date is null`() {
        loanDto.date = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            loanDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when value is null`() {
        loanDto.value = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            loanDto.validateDataForDbInsertion()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // toModel()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return an model object when call toModel`() {
        val expected = sampleLoan()

        val model = loanDto.toModel()

        Assert.assertEquals(expected, model)
    }

}