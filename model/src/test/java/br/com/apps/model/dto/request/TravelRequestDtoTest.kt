package br.com.apps.model.dto.request

import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class TravelRequestDtoTest {

    private lateinit var requestDto: TravelRequestDto

    @Before
    fun setup() {
        requestDto = TravelRequestDto(
            masterUid = "1",
            id = "2",
            truckId = "3",
            driverId = "4",
            encodedImage = "encodedImage",
            date = Date(),
            requestNumber = 123,
            status = "SENT"
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        requestDto.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            requestDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        requestDto.id = null
        assertThrows(CorruptedFileException::class.java) {
            requestDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when truckId is null`() {
        requestDto.truckId = null
        assertThrows(CorruptedFileException::class.java) {
            requestDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when driverId is null`() {
        requestDto.driverId = null
        assertThrows(CorruptedFileException::class.java) {
            requestDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        requestDto.date = null
        assertThrows(CorruptedFileException::class.java) {
            requestDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when requestNumber is null`() {
        requestDto.requestNumber = null
        assertThrows(CorruptedFileException::class.java) {
            requestDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when status is null`() {
        requestDto.status = null
        assertThrows(CorruptedFileException::class.java) {
            requestDto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validateForDataBaseInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for database insertion`() {
        requestDto.masterUid = null
        assertThrows(InvalidForSavingException::class.java) {
            requestDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when truckId is null for database insertion`() {
        requestDto.truckId = null
        assertThrows(InvalidForSavingException::class.java) {
            requestDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when driverId is null for database insertion`() {
        requestDto.driverId = null
        assertThrows(InvalidForSavingException::class.java) {
            requestDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when date is null for database insertion`() {
        requestDto.date = null
        assertThrows(InvalidForSavingException::class.java) {
            requestDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when requestNumber is null for database insertion`() {
        requestDto.requestNumber = null
        assertThrows(InvalidForSavingException::class.java) {
            requestDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when status is null for database insertion`() {
        requestDto.status = null
        assertThrows(InvalidForSavingException::class.java) {
            requestDto.validateDataForDbInsertion()
        }
    }

}