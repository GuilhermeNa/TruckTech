package br.com.apps.model.dto.payroll

import br.com.apps.model.exceptions.CorruptedFileException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Date

class AdvanceDtoTest {

    private lateinit var advanceDto: AdvanceDto

    @Before
    fun setup() {
        advanceDto = AdvanceDto(
            masterUid = "1",
            id = "2",
            travelId = "3",
            employeeId = "4",
            date = Date(),
            value = 10.0,
            isPaid = true,
            isApproved = true,
            type = "COMMISSION"
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        advanceDto.masterUid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        advanceDto.id = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when employeeId is null`() {
        advanceDto.employeeId = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        advanceDto.date = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        advanceDto.value = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isPaid is null`() {
        advanceDto.isPaid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isApproved is null`() {
        advanceDto.isApproved = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when type is null`() {
        advanceDto.type = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            advanceDto.validateDataIntegrity()
        }
    }

}