package br.com.apps.model.dto.travel

import br.com.apps.model.toDate
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

class RefuelDtoTest {

    private val refuel = RefuelDto(
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
        isCompleteRefuel = true
    )

    @Test
    fun `should return true if there is no null field in non-null model fields`() {
        val isValid = refuel.validateFields()
        Assert.assertTrue(isValid)
    }

    @Test
    fun `should return false if there is any null field in non-null model fields`() {
        val dtoA = RefuelDto(
            masterUid = null, id = "2", truckId = "3", travelId = "4", driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station", odometerMeasure = 10.0, valuePerLiter = 4.5, amountLiters = 2.0,
            totalValue = 9.0, isCompleteRefuel = true
        )
        val dtoB = RefuelDto(
            masterUid = "1", id = "2", truckId = null, travelId = "4", driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station", odometerMeasure = 10.0, valuePerLiter = 4.5, amountLiters = 2.0,
            totalValue = 9.0, isCompleteRefuel = true
        )
        val dtoC = RefuelDto(
            masterUid = "1", id = "2", truckId = "3", travelId = null, driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station", odometerMeasure = 10.0, valuePerLiter = 4.5, amountLiters = 2.0,
            totalValue = 9.0, isCompleteRefuel = true
        )
        val dtoD = RefuelDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = null,
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station", odometerMeasure = 10.0, valuePerLiter = 4.5, amountLiters = 2.0,
            totalValue = 9.0, isCompleteRefuel = true
        )
        val dtoE = RefuelDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            date = null, station = "Station", odometerMeasure = 10.0, valuePerLiter = 4.5,
            amountLiters = 2.0, totalValue = 9.0, isCompleteRefuel = true
        )
        val dtoF = RefuelDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = null, odometerMeasure = 10.0, valuePerLiter = 4.5, amountLiters = 2.0,
            totalValue = 9.0, isCompleteRefuel = true
        )
        val dtoG = RefuelDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station", odometerMeasure = null, valuePerLiter = 4.5, amountLiters = 2.0,
            totalValue = 9.0, isCompleteRefuel = true
        )
        val dtoH = RefuelDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station", odometerMeasure = 10.0, valuePerLiter = 4.5, amountLiters = null,
            totalValue = 9.0, isCompleteRefuel = true
        )
        val dtoI = RefuelDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station", odometerMeasure = null, valuePerLiter = 4.5, amountLiters = 2.0,
            totalValue = null, isCompleteRefuel = true
        )
        val dtoJ = RefuelDto(
            masterUid = "1", id = "2", truckId = "3", travelId = "4", driverId = "5",
            date = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            station = "Station", odometerMeasure = null, valuePerLiter = 4.5, amountLiters = 2.0,
            totalValue = 9.0, isCompleteRefuel = null
        )

        val isValidA = dtoA.validateFields()
        val isValidB = dtoB.validateFields()
        val isValidC = dtoC.validateFields()
        val isValidD = dtoD.validateFields()
        val isValidE = dtoE.validateFields()
        val isValidF = dtoF.validateFields()
        val isValidG = dtoG.validateFields()
        val isValidH = dtoH.validateFields()
        val isValidI = dtoI.validateFields()
        val isValidJ = dtoJ.validateFields()

        Assert.assertFalse(isValidA)
        Assert.assertFalse(isValidB)
        Assert.assertFalse(isValidC)
        Assert.assertFalse(isValidD)
        Assert.assertFalse(isValidE)
        Assert.assertFalse(isValidF)
        Assert.assertFalse(isValidG)
        Assert.assertFalse(isValidH)
        Assert.assertFalse(isValidI)
        Assert.assertFalse(isValidJ)
    }

}