package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.user.AccessLevel
import br.com.apps.model.test_cases.sampleFreightDto
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class FreightDtoTest {

    private lateinit var freightDto: FreightDto

    @Before
    fun setup() {
      freightDto = sampleFreightDto()
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        freightDto.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when truckId is null`() {
        freightDto.truckId = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when travelId is null`() {
        freightDto.travelId = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when driverId is null`() {
        freightDto.employeeId = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when customerId is null`() {
        freightDto.customerId = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when origin is null`() {
        freightDto.origin = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when destiny is null`() {
        freightDto.destiny = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when weight is null`() {
        freightDto.weight = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when cargo is null`() {
        freightDto.cargo = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when value is null`() {
        freightDto.value = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when loadingDate is null`() {
        freightDto.loadingDate = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }


    @Test
    fun `should throw CorruptedFileException when commissionPercentual is null`() {
        freightDto.commissionPercentual = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isValid is null`() {
        freightDto.isValid = null
        assertThrows(CorruptedFileException::class.java) {
            freightDto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // isReadyForDataBaseInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for dataBase insertion`() {
        freightDto.masterUid = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when truckId is null for dataBase insertion`() {
        freightDto.truckId = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when travelId is null for dataBase insertion`() {
        freightDto.travelId = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when driverId is null for dataBase insertion`() {
        freightDto.employeeId = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when customerId is null for dataBase insertion`() {
        freightDto.customerId = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when origin is null for dataBase insertion`() {
        freightDto.origin = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when destiny is null for dataBase insertion`() {
        freightDto.destiny = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when weight is null for dataBase insertion`() {
        freightDto.weight = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when cargo is null for dataBase insertion`() {
        freightDto.cargo = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when value is null for dataBase insertion`() {
        freightDto.value = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when loadingDate is null for dataBase insertion`() {
        freightDto.loadingDate = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when commissionPercentual is null for dataBase insertion`() {
        freightDto.commissionPercentual = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isValid is null for dataBase insertion`() {
        freightDto.isValid = null
        assertThrows(InvalidForSavingException::class.java) {
            freightDto.validateDataForDbInsertion()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validatePermission()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `validatePermission() - should throw NullPointerException when authLevel is null`() {
        assertThrows(NullPointerException::class.java) {
            freightDto.validateWriteAccess(null)
        }
    }

    @Test
    fun `validatePermission() - should throw NullPointerException when isValid is null`() {
        freightDto.isValid = null
        assertThrows(NullPointerException::class.java) {
            freightDto.validateWriteAccess(AccessLevel.MANAGER)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is OPERATIONAL`() {
        freightDto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            freightDto.validateWriteAccess(AccessLevel.OPERATIONAL)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is TRAINEE`() {
        freightDto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            freightDto.validateWriteAccess(AccessLevel.TRAINEE)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is ADMIN_ASSISTANT`() {
        freightDto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            freightDto.validateWriteAccess(AccessLevel.ADMIN_ASSISTANT)
        }
    }
}