package br.com.apps.model.dto.request

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.test_cases.sampleRequestDto
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class RequestDtoTest {

    private lateinit var dto: RequestDto

    @Before
    fun setup() {
        dto = sampleRequestDto()
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
    fun `should throw CorruptedFileException when uid is null`() {
        val dto = dto.copy(uid = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when requestNumber is null`() {
        val dto = dto.copy(requestNumber = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        val dto = dto.copy(date = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when status is null`() {
        val dto = dto.copy(status = null)

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
    fun `should throw InvalidForSavingException when uid is null for database insertion`() {
        val modifiedDto = dto.copy(uid = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when requestNumber is null for database insertion`() {
        val modifiedDto = dto.copy(requestNumber = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when date is null for database insertion`() {
        val modifiedDto = dto.copy(date = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when status is null for database insertion`() {
        val modifiedDto = dto.copy(status = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

}