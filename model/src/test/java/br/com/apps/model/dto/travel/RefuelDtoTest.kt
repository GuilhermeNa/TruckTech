package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.user.AccessLevel
import br.com.apps.model.test_cases.sampleRefuelDto
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class RefuelDtoTest {

    private lateinit var dto: RefuelDto

    @Before
    fun setup() {
        dto = sampleRefuelDto()
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        dto.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when truckId is null`() {
        dto.truckId = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when travelId is null`() {
        dto.travelId = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        dto.date = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when station is null`() {
        dto.station = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when odometerMeasure is null`() {
        dto.odometerMeasure = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when valuePerLiter is null`() {
        dto.valuePerLiter = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when amountLiters is null`() {
        dto.amountLiters = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when totalValue is null`() {
        dto.totalValue = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isCompleteRefuel is null`() {
        dto.isCompleteRefuel = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isValid is null`() {
        dto.isValid = null
        assertThrows(CorruptedFileException::class.java) {
            dto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // isReadyForDataBaseInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for dataBase insertion`() {
        dto.masterUid = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when truckId is null for dataBase insertion`() {
        dto.truckId = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when travelId is null for dataBase insertion`() {
        dto.travelId = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when date is null for dataBase insertion`() {
        dto.date = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when station is null for dataBase insertion`() {
        dto.station = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when odometerMeasure is null for dataBase insertion`() {
        dto.odometerMeasure = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when valuePerLiter is null for dataBase insertion`() {
        dto.valuePerLiter = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when amountLiters is null for dataBase insertion`() {
        dto.amountLiters = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when totalValue is null for dataBase insertion`() {
        dto.totalValue = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isCompleteRefuel is null for dataBase insertion`() {
        dto.isCompleteRefuel = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isValid is null for dataBase insertion`() {
        dto.isValid = null
        assertThrows(InvalidForSavingException::class.java) {
            dto.validateDataForDbInsertion()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validatePermission()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `validatePermission() - should throw NullPointerException when authLevel is null`() {
        assertThrows(NullPointerException::class.java) {
            dto.validateWriteAccess(null)
        }
    }

    @Test
    fun `validatePermission() - should throw NullPointerException when isValid is null`() {
        dto.isValid = null
        assertThrows(NullPointerException::class.java) {
            dto.validateWriteAccess(AccessLevel.MANAGER)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is OPERATIONAL`() {
        dto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            dto.validateWriteAccess(AccessLevel.OPERATIONAL)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is TRAINEE`() {
        dto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            dto.validateWriteAccess(AccessLevel.TRAINEE)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is ADMIN_ASSISTANT`() {
        dto.isValid = true
        assertThrows(AccessLevelException::class.java) {
            dto.validateWriteAccess(AccessLevel.ADMIN_ASSISTANT)
        }
    }

}