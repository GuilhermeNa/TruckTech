package br.com.apps.model.dto.request

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RequestItemDtoTest {

    private lateinit var requestItemDto: RequestItemDto

    @Before
    fun setup() {
        requestItemDto = RequestItemDto(
            id = "1",
            labelId = "2",
            requestId = "3",
            docUrl = "docUrl",
            kmMarking = 100,
            value = 500.0,
            type = "REFUEL"
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        requestItemDto.id = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            requestItemDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when requestId is null`() {
        requestItemDto.requestId = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            requestItemDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        requestItemDto.value = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            requestItemDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when type is null`() {
        requestItemDto.type = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            requestItemDto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validateForDataBaseInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when requestId is null for dataBase insertion`() {
        requestItemDto.requestId = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            requestItemDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when value is null for dataBase insertion`() {
        requestItemDto.value = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            requestItemDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when type is null for dataBase insertion`() {
        requestItemDto.type = null
        Assert.assertThrows(InvalidForSavingException::class.java) {
            requestItemDto.validateForDataBaseInsertion()
        }
    }

}