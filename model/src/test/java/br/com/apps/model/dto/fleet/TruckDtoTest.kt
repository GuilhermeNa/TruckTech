package br.com.apps.model.dto.fleet

import br.com.apps.model.exceptions.CorruptedFileException
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TruckDtoTest {

    private lateinit var truckDto: TruckDto

    @Before
    fun setup() {
        truckDto = TruckDto(
            masterUid = "1",
            id = "2",
            employeeId = "3",
            averageAim = 2.5,
            performanceAim = 45.0,
            plate = "PLATE01",
            color = "White",
            commissionPercentual = 10.0,
            type = "TRUCK"
        )
    }

    //---------------------------------------------------------------------------------------------//
    // validateDataIntegrity()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw CorruptedFileException when masterUid is null`() {
        truckDto.masterUid = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when id is null`() {
        truckDto.id = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when plate is null`() {
        truckDto.plate = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when fleetType is null`() {
        truckDto.type = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when driverId is null`() {
        truckDto.employeeId = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when averageAim is null`() {
        truckDto.averageAim = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when performanceAim is null`() {
        truckDto.performanceAim = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when color is null`() {
        truckDto.color = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

    @Test
    fun `should throw CorruptedFileException when commissionPercentual is null`() {
        truckDto.commissionPercentual = null
        Assert.assertThrows(CorruptedFileException::class.java) {
            truckDto.validateDataIntegrity()
        }
    }

}