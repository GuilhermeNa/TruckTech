package br.com.apps.model.dto.fleet

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class TrailerDtoTest {

    private lateinit var trailerDto: TrailerDto

    @Before
    fun setup() {
        trailerDto = TrailerDto(
            masterUid = "1",
            id = "2",
            plate = "PLATE01",
            type = "FOUR_AXIS",
            truckId = "3"
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        trailerDto.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        trailerDto.id = null
        assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when plate is null`() {
        trailerDto.plate = null
        assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when type is null`() {
        trailerDto.type = null
        assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // isReadyForDataBaseInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for dataBase insertion`() {
        trailerDto.masterUid = null
        assertThrows(InvalidForSavingException::class.java) {
            trailerDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when plate is null for dataBase insertion`() {
        trailerDto.plate = null
        assertThrows(InvalidForSavingException::class.java) {
            trailerDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when type is null for dataBase insertion`() {
        trailerDto.type = null
        assertThrows(InvalidForSavingException::class.java) {
            trailerDto.validateDataForDbInsertion()
        }
    }

}