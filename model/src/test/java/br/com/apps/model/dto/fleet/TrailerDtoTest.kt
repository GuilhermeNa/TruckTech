package br.com.apps.model.dto.fleet

import br.com.apps.model.exceptions.CorruptedFileException
import org.junit.Assert
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
            fleetType = "FOUR_AXIS",
            truckId = "3"
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        trailerDto.masterUid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        trailerDto.id = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when plate is null`() {
        trailerDto.plate = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when truckId is null`() {
        trailerDto.truckId = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when fleetType is null`() {
        trailerDto.fleetType = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            trailerDto.validateDataIntegrity()
        }
    }

}