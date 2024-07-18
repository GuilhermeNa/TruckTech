package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidAuthLevelException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.user.PermissionLevelType
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class ExpendDtoTest {

    private lateinit var expendDto: ExpendDto

    @Before
    fun setup() {
        expendDto = ExpendDto(
            masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "5", labelId = "6",
            company = "Company", date = Date(), description = "Description", value = 100.0,
            label = null, isPaidByEmployee = true, isAlreadyRefunded = true, isValid = true
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        expendDto.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when truckId is null`() {
        expendDto.truckId = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when driverId is null`() {
        expendDto.driverId = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when travelId is null`() {
        expendDto.travelId = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when labelId is null`() {
        expendDto.labelId = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when company is null`() {
        expendDto.company = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        expendDto.date = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when description is null`() {
        expendDto.description = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        expendDto.value = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isPaidByEmployee is null`() {
        expendDto.isPaidByEmployee = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isAlreadyRefunded is null`() {
        expendDto.isAlreadyRefunded = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isValid is null`() {
        expendDto.isValid = null
        assertThrows(CorruptedFileException::class.java) {
            expendDto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validateForDataBaseInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for database insertion`() {
        expendDto.masterUid = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when truckId is null for database insertion`() {
        expendDto.truckId = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when driverId is null for database insertion`() {
        expendDto.driverId = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when travelId is null for database insertion`() {
        expendDto.travelId = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when labelId is null for database insertion`() {
        expendDto.labelId = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when company is null for database insertion`() {
        expendDto.company = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when date is null for database insertion`() {
        expendDto.date = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when description is null for database insertion`() {
        expendDto.description = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when value is null for database insertion`() {
        expendDto.value = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isPaidByEmployee is null for database insertion`() {
        expendDto.isPaidByEmployee = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isAlreadyRefunded is null for database insertion`() {
        expendDto.isAlreadyRefunded = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isValid is null for database insertion`() {
        expendDto.isValid = null
        assertThrows(InvalidForSavingException::class.java) {
            expendDto.validateForDataBaseInsertion()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validatePermission()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw NullPointerException when authLevel is null`() {
        assertThrows(NullPointerException::class.java) {
            expendDto.validatePermission(null)
        }
    }

    @Test
    fun `should throw NullPointerException when isValid is null`() {
        expendDto.isValid = null
        assertThrows(NullPointerException::class.java) {
            expendDto.validatePermission(PermissionLevelType.MANAGER)
        }
    }

    @Test
    fun `should throw InvalidAuthLevelException when authLevel is OPERATIONAL`() {
        expendDto.isValid = true
        assertThrows(InvalidAuthLevelException::class.java) {
            expendDto.validatePermission(PermissionLevelType.OPERATIONAL)
        }
    }

    @Test
    fun `should throw InvalidAuthLevelException when authLevel is TRAINEE`() {
        expendDto.isValid = true
        assertThrows(InvalidAuthLevelException::class.java) {
            expendDto.validatePermission(PermissionLevelType.TRAINEE)
        }
    }

    @Test
    fun `should throw InvalidAuthLevelException when authLevel is ADMIN_ASSISTANT`() {
        expendDto.isValid = true
        assertThrows(InvalidAuthLevelException::class.java) {
            expendDto.validatePermission(PermissionLevelType.ADMIN_ASSISTANT)
        }
    }

}