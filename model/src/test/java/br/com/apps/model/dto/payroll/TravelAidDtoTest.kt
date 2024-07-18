package br.com.apps.model.dto.payroll

import br.com.apps.model.exceptions.CorruptedFileException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Date

class TravelAidDtoTest {

    private lateinit var travelAidDto: TravelAidDto

    @Before
    fun setup() {
        travelAidDto = TravelAidDto(
            masterUid = "1",
            id = "2",
            driverId = "3",
            travelId = "4",
            date = Date(),
            value = 500.0,
            isPaid = false
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        travelAidDto.masterUid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            travelAidDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        travelAidDto.id = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            travelAidDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when driverId is null`() {
        travelAidDto.driverId = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            travelAidDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when travelId is null`() {
        travelAidDto.travelId = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            travelAidDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        travelAidDto.date = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            travelAidDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        travelAidDto.value = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            travelAidDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isPaid is null`() {
        travelAidDto.isPaid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            travelAidDto.validateDataIntegrity()
        }
    }

}