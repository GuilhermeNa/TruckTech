package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.util.toDate
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TravelDtoTest {

    private lateinit var travelDto: TravelDto

    @Before
    fun setup() {
        travelDto = TravelDto(
            masterUid = "1",
            id = "2",
            truckId = "3",
            employeeId = "4",

            isFinished = true,
            isClosed = true,
            initialDate = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            finalDate = LocalDateTime.of(2022, 1, 15, 12, 0).toDate(),
            initialOdometer = 10.0,
            finalOdometer = 50.0,
        )
    }


    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        travelDto.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            travelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when truckId is null`() {
        travelDto.truckId = null
        assertThrows(CorruptedFileException::class.java) {
            travelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when driverId is null`() {
        travelDto.employeeId = null
        assertThrows(CorruptedFileException::class.java) {
            travelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isFinished is null`() {
        travelDto.isFinished = null
        assertThrows(CorruptedFileException::class.java) {
            travelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when considerAverage is null`() {
        travelDto.isClosed = null
        assertThrows(CorruptedFileException::class.java) {
            travelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when initialDate is null`() {
        travelDto.initialDate = null
        assertThrows(CorruptedFileException::class.java) {
            travelDto.validateDataIntegrity()
        }
    }

}