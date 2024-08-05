package br.com.apps.model.dto.finance

import br.com.apps.model.dto.finance.payable.EmployeePayableDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.test_cases.sampleEmployeePayable
import br.com.apps.model.test_cases.sampleEmployeePayableDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class EmployeePayableDtoTest {

    private lateinit var dto: EmployeePayableDto

    @Before
    fun setup() { dto = sampleEmployeePayableDto() }

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
    fun `should throw CorruptedFileException when value is null`() {
        dto = dto.copy(value = null)
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
    fun `should throw CorruptedFileException when installments is null`() {
        dto = dto.copy(installments = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isPaid is null`() {
        dto = dto.copy(isPaid = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when employeeId is null`() {
        dto = dto.copy(employeeId = null)
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
    fun `should throw InvalidForSavingException when isPaid is null for db insertion`() {
        dto = dto.copy(isPaid = null)
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
        val expectedModel = sampleEmployeePayable()

        val model = dto.toModel()

        assertEquals(expectedModel, model)
    }

}