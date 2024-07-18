package br.com.apps.model.dto

import br.com.apps.model.exceptions.CorruptedFileException
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class LabelDtoTest {

    private lateinit var labelDto: LabelDto

    @Before
    fun setup() {
        labelDto = LabelDto(
            masterUid = "1",
            id = "2",
            name = "Expense label",
            urlIcon = "urlImage",
            color = 123,
            type = "EXPENSE",
            isDefaultLabel = false,
            isOperational = true
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        labelDto.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            labelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        labelDto.id = null
        assertThrows(CorruptedFileException::class.java) {
            labelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when name is null`() {
        labelDto.name = null
        assertThrows(CorruptedFileException::class.java) {
            labelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when type is null`() {
        labelDto.type = null
        assertThrows(CorruptedFileException::class.java) {
            labelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isDefaultLabel is null`() {
        labelDto.isDefaultLabel = null
        assertThrows(CorruptedFileException::class.java) {
            labelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isOperational is null`() {
        labelDto.isOperational = null
        assertThrows(CorruptedFileException::class.java) {
            labelDto.validateDataIntegrity()
        }
    }

}