package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.user.AccessLevel
import br.com.apps.model.test_cases.sampleOutlayDto
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class OutlayDtoTest {

    private lateinit var dto: OutlayDto

    @Before
    fun setup() {
        dto = sampleOutlayDto()
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
    fun `should throw CorruptedFileException when truckId is null`() {
        val dto = dto.copy(truckId = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when driverId is null`() {
        val dto = dto.copy(employeeId = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when travelId is null`() {
        val dto = dto.copy(travelId = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when labelId is null`() {
        val dto = dto.copy(labelId = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when company is null`() {
        val dto = dto.copy(company = null)

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
    fun `should throw CorruptedFileException when description is null`() {
        val dto = dto.copy(description = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        val dto = dto.copy(value = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isPaidByEmployee is null`() {
        val dto = dto.copy(isPaidByEmployee = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isValid is null`() {
        val dto = dto.copy(isValid = null)

        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validateForDataBaseInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for database insertion`() {
        val modifiedDto = dto.copy(masterUid = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when truckId is null for database insertion`() {
        val modifiedDto = dto.copy(truckId = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when driverId is null for database insertion`() {
        val modifiedDto = dto.copy(employeeId = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when travelId is null for database insertion`() {
        val modifiedDto = dto.copy(travelId = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when labelId is null for database insertion`() {
        val modifiedDto = dto.copy(labelId = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when company is null for database insertion`() {
        val modifiedDto = dto.copy(company = null)
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
    fun `should throw InvalidForSavingException when description is null for database insertion`() {
        val modifiedDto = dto.copy(description = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when value is null for database insertion`() {
        val modifiedDto = dto.copy(value = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isPaidByEmployee is null for database insertion`() {
        val modifiedDto = dto.copy(isPaidByEmployee = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isValid is null for database insertion`() {
        val modifiedDto = dto.copy(isValid = null)
        assertThrows(InvalidForSavingException::class.java) {
            modifiedDto.validateDataForDbInsertion()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validatePermission()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw NullPointerException when authLevel is null`() {
        assertThrows(NullPointerException::class.java) {
            dto.validateWriteAccess(null)
        }
    }

    @Test
    fun `should throw NullPointerException when isValid is null`() {
        dto.isValid = null
        assertThrows(NullPointerException::class.java) {
            dto.validateWriteAccess(AccessLevel.MANAGER)
        }
    }

    @Test
    fun `should throw InvalidAuthLevelException when authLevel is OPERATIONAL`() {
        dto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            dto.validateWriteAccess(AccessLevel.OPERATIONAL)
        }
    }

    @Test
    fun `should throw InvalidAuthLevelException when authLevel is TRAINEE`() {
        dto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            dto.validateWriteAccess(AccessLevel.TRAINEE)
        }
    }

    @Test
    fun `should throw InvalidAuthLevelException when authLevel is ADMIN_ASSISTANT`() {
        dto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            dto.validateWriteAccess(AccessLevel.ADMIN_ASSISTANT)
        }
    }

}