package br.com.apps.model.dto

import br.com.apps.model.dto.bank.BankDto
import br.com.apps.model.exceptions.CorruptedFileException
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BankDtoTest {

    private lateinit var bankDto: BankDto

    @Before
    fun setup() {
        bankDto = BankDto(
            id = "1",
            name = "Bradesco",
            code = 123,
            urlImage = "urlImage"
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        bankDto.id = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            bankDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when name is null`() {
        bankDto.name = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            bankDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when code is null`() {
        bankDto.code = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            bankDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when urlImage is null`() {
        bankDto.urlImage = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            bankDto.validateDataIntegrity()
        }
    }

}