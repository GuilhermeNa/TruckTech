package br.com.apps.model.dto.request

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.test_cases.sampleItemDto
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class ItemDtoTest {

    private lateinit var dto: ItemDto

    @Before
    fun setup() {
        dto = sampleItemDto()
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        val dto = dto.copy(masterUid = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        val dto = dto.copy(id = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when parentId is null`() {
        val dto = dto.copy(parentId = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        val dto = dto.copy(value = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when description is null`() {
        val dto = dto.copy(description = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataForDbInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for database insertion`() {
        val modifiedDto = dto.copy(masterUid = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when parentId is null for database insertion`() {
        val modifiedDto = dto.copy(parentId = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when value is null for database insertion`() {
        val modifiedDto = dto.copy(value = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when description is null for database insertion`() {
        val modifiedDto = dto.copy(description = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

}