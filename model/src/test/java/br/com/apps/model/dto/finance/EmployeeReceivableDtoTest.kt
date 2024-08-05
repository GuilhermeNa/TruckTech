package br.com.apps.model.dto.finance

import br.com.apps.model.dto.finance.receivable.EmployeeReceivableDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.test_cases.sampleEmployeeReceivable
import br.com.apps.model.test_cases.sampleEmployeeReceivableDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class EmployeeReceivableDtoTest {

    private lateinit var dto: EmployeeReceivableDto

    @Before
    fun setup() { dto = sampleEmployeeReceivableDto() }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        dto = dto.copy(masterUid = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        dto = dto.copy(id = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when parentId is null`() {
        dto = dto.copy(parentId = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when installments is null`() {
        dto = dto.copy(installments = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        dto = dto.copy(value = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when type is null`() {
        dto = dto.copy(type = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when generationDate is null`() {
        dto = dto.copy(generationDate = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isReceived is null`() {
        dto = dto.copy(isReceived = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataForDbInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for db insertion`() {
        dto = dto.copy(masterUid = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when parentId is null for db insertion`() {
        dto = dto.copy(parentId = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when value is null for db insertion`() {
        dto = dto.copy(value = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when generationDate is null for db insertion`() {
        dto = dto.copy(generationDate = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when installments is null for db insertion`() {
        dto = dto.copy(installments = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isReceived is null for db insertion`() {
        dto = dto.copy(isReceived = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when employeeId is null for db insertion`() {
        dto = dto.copy(employeeId = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when type is null for db insertion`() {
        dto = dto.copy(type = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // toModel()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return a dto object with correspondent data`() {
        val expectedModel = sampleEmployeeReceivable()

        val model = dto.toModel()

        assertEquals(expectedModel, model)
    }

}