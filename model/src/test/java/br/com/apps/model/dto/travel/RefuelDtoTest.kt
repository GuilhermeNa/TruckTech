package br.com.apps.model.dto.travel

import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.InvalidAuthLevelException
import br.com.apps.model.exceptions.InvalidForSavingException
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.model.toDate
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class RefuelDtoTest {

    private lateinit var refuelDto: RefuelDto

    @Before
    fun setup() {
        refuelDto = RefuelDto(
            masterUid = "1",
            id = "2",
            truckId = "3",
            travelId = "4",
            driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station",
            odometerMeasure = 10.0,
            valuePerLiter = 4.5,
            amountLiters = 2.0,
            totalValue = 9.0,
            isCompleteRefuel = true,
            isValid = true
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        refuelDto.masterUid = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when truckId is null`() {
        refuelDto.truckId = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when travelId is null`() {
        refuelDto.travelId = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when driverId is null`() {
        refuelDto.driverId = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when date is null`() {
        refuelDto.date = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when station is null`() {
        refuelDto.station = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when odometerMeasure is null`() {
        refuelDto.odometerMeasure = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when valuePerLiter is null`() {
        refuelDto.valuePerLiter = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when amountLiters is null`() {
        refuelDto.amountLiters = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when totalValue is null`() {
        refuelDto.totalValue = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isCompleteRefuel is null`() {
        refuelDto.isCompleteRefuel = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when isValid is null`() {
        refuelDto.isValid = null
        assertThrows(CorruptedFileException::class.java) {
            refuelDto.validateDataIntegrity()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // isReadyForDataBaseInsertion()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw InvalidForSavingException when masterUid is null for dataBase insertion`() {
        refuelDto.masterUid = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when truckId is null for dataBase insertion`() {
        refuelDto.truckId = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when travelId is null for dataBase insertion`() {
        refuelDto.travelId = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when driverId is null for dataBase insertion`() {
        refuelDto.driverId = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when date is null for dataBase insertion`() {
        refuelDto.date = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when station is null for dataBase insertion`() {
        refuelDto.station = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when odometerMeasure is null for dataBase insertion`() {
        refuelDto.odometerMeasure = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when valuePerLiter is null for dataBase insertion`() {
        refuelDto.valuePerLiter = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when amountLiters is null for dataBase insertion`() {
        refuelDto.amountLiters = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when totalValue is null for dataBase insertion`() {
        refuelDto.totalValue = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isCompleteRefuel is null for dataBase insertion`() {
        refuelDto.isCompleteRefuel = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    @Test
    fun `should throw InvalidForSavingException when isValid is null for dataBase insertion`() {
        refuelDto.isValid = null
        assertThrows(InvalidForSavingException::class.java) {
            refuelDto.validateForDataBaseInsertion()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // validatePermission()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `validatePermission() - should throw NullPointerException when authLevel is null`() {
        assertThrows(NullPointerException::class.java) {
            refuelDto.validatePermission(null)
        }
    }

    @Test
    fun `validatePermission() - should throw NullPointerException when isValid is null`() {
        refuelDto.isValid = null
        assertThrows(NullPointerException::class.java) {
            refuelDto.validatePermission(PermissionLevelType.MANAGER)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is OPERATIONAL`() {
        refuelDto.isValid = true
        assertThrows(InvalidAuthLevelException::class.java) {
            refuelDto.validatePermission(PermissionLevelType.OPERATIONAL)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is TRAINEE`() {
        refuelDto.isValid = true
        assertThrows(InvalidAuthLevelException::class.java) {
            refuelDto.validatePermission(PermissionLevelType.TRAINEE)
        }
    }

    @Test
    fun `validatePermission() - should throw InvalidAuthLevelException when authLevel is ADMIN_ASSISTANT`() {
        refuelDto.isValid = true
        assertThrows(InvalidAuthLevelException::class.java) {
            refuelDto.validatePermission(PermissionLevelType.ADMIN_ASSISTANT)
        }
    }

}