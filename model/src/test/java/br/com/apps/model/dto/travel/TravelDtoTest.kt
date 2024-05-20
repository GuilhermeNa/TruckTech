package br.com.apps.model.dto.travel

import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.toDate
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

class TravelDtoTest {

    private val travel = TravelDto(
        masterUid = "1",
        id = "2",
        truckId = "3",
        driverId = "4",

        isFinished = true,
        initialDate = LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
        finalDate = LocalDateTime.of(2022, 1, 15, 12, 0).toDate(),
        initialOdometerMeasurement = 10.0,
        finalOdometerMeasurement = 50.0,
        freightsList = listOf(Freight(id = "1"), Freight(id = "2")),
        refuelsList = listOf(Refuel(id = "3"), Refuel(id = "4"), Refuel(id = "5")),
        expendsList = listOf(Expend(id = "6"))
    )

    @Test
    fun `should return true if there is no null field in non-null model fields`() {
        val isValid = travel.validateFields()
        Assert.assertTrue(isValid)
    }

    @Test
    fun `should return false if there is any null field in non-null model fields`() {
        val dtoA = TravelDto(null, "2", "3", "4", false,
            LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            null, 10.0)
        val dtoB = TravelDto(
            "1", "2", null, "4", false,
            LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            null, 10.0 )
        val dtoC = TravelDto(
            "1", "2", "3", null, false,
            LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            null, 10.0)
        val dtoD = TravelDto(
            "1", "2", "3", "4", null,
            LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            null, 10.0)
        val dtoE = TravelDto(
            "1", "2", "3", "4", false,
            null, null, 10.0)
        val dtoF = TravelDto(
            "1", "2", "3", "4", false,
            LocalDateTime.of(2022, 1, 1, 12, 0).toDate(),
            null, null)

        val isValidA = dtoA.validateFields()
        val isValidB = dtoB.validateFields()
        val isValidC = dtoC.validateFields()
        val isValidD = dtoD.validateFields()
        val isValidE = dtoE.validateFields()
        val isValidF = dtoF.validateFields()

        Assert.assertFalse(isValidA)
        Assert.assertFalse(isValidB)
        Assert.assertFalse(isValidC)
        Assert.assertFalse(isValidD)
        Assert.assertFalse(isValidE)
        Assert.assertFalse(isValidF)
    }



}