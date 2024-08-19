package br.com.apps.model.dto.finance

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.test_cases.sampleTransaction
import br.com.apps.model.test_cases.sampleTransactionDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class TransactionDtoTest {

    private lateinit var dto: TransactionDto

    @Before
    fun setup() { dto = sampleTransactionDto() }

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
    fun `should throw CorruptedFileException when parentKey is null`() {
        dto = dto.copy(parentId = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when dueDate is null`() {
        dto = dto.copy(dueDate = null)
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when number is null`() {
        dto = dto.copy(number = null)
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
    fun `should throw CorruptedFileException when isPaid is null`() {
        dto = dto.copy(isPaid = null)
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
    fun `should throw InvalidForSavingException when parentKey is null for db insertion`() {
        dto = dto.copy(parentId = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when dueDate is null for db insertion`() {
        dto = dto.copy(dueDate = null)
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when number is null for db insertion`() {
        dto = dto.copy(number = null)
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
    fun `should throw InvalidForSavingException when type is null for db insertion`() {
        dto = dto.copy(type = null)
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

    //---------------------------------------------------------------------------------------------//
    // toModel()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return a model object with correspondent data`() {
        val expectedModel = sampleTransaction()

        val model = dto.toModel()

        assertEquals(expectedModel, model)
    }

}