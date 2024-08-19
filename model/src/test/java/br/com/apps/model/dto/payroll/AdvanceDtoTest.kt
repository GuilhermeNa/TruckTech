package br.com.apps.model.dto.payroll

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.test_cases.sampleAdvance
import br.com.apps.model.test_cases.sampleAdvanceDto
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AdvanceDtoTest {

    private lateinit var advanceDto: AdvanceDto

    @Before
    fun setup() {
        advanceDto = sampleAdvanceDto()
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        advanceDto.masterUid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        advanceDto.id = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when employeeId is null`() {
        advanceDto.employeeId = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        advanceDto.date = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        advanceDto.value = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when type is null`() {
        advanceDto.type = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // InvalidForSavingException()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null`() {
        advanceDto.masterUid = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            advanceDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when employeeId is null`() {
        advanceDto.employeeId = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            advanceDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when date is null`() {
        advanceDto.date = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            advanceDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when value is null`() {
        advanceDto.value = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            advanceDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when type is null`() {
        advanceDto.type = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            advanceDto.validateDataForDbInsertion()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // toModel()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return a advance model when call toModel`() {
        val expected = sampleAdvance()

        val model = advanceDto.toModel()

        assertEquals(expected, model)
    }

}