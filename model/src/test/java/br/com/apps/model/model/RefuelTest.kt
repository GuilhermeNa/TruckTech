package br.com.apps.model.model

import br.com.apps.model.model.travel.Refuel
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class RefuelTest {

    private val refuel = Refuel(
        masterUid = "1",
        id = "2",
        truckId = "3",
        driverId = "4",
        travelId = "5",
        costId = "6",

        date = LocalDateTime.of(2022, 1, 1, 12, 0),
        station = "Posto",
        odometerMeasure = BigDecimal(1000),
        valuePerLiter = BigDecimal(5),
        amountLiters = BigDecimal(500),
        totalValue = BigDecimal(2500),
        isCompleteRefuel = false
        )

    @Test
    fun `should return true when all ids are valid`() {
        val isIdsValid = refuel.validateIds()
        Assert.assertTrue(isIdsValid)
    }

    @Test
    fun `should return false when any id is null or empty`() {
        val refuelWithMasterUidNull = Refuel(masterUid = null, id = "2", truckId = "3", driverId = "4", travelId = "5")
        val refuelWithIdNull = Refuel(masterUid = "1", id = null, truckId = "3", driverId = "4", travelId = "5")
        val refuelWithTruckIdNull = Refuel(masterUid = "1", id = "2", truckId = null, driverId = "4", travelId = "5")
        val refuelWithDriverIdNull = Refuel(masterUid = "1", id = "2", truckId = "3", driverId = null, travelId = "5")
        val refuelWithTravelIdNull = Refuel(masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = null)
        val isValidA = refuelWithMasterUidNull.validateIds()
        val isValidB = refuelWithIdNull.validateIds()
        val isValidC = refuelWithTruckIdNull.validateIds()
        val isValidD = refuelWithDriverIdNull.validateIds()
        val isValidE = refuelWithTravelIdNull.validateIds()
        Assert.assertFalse(isValidA)
        Assert.assertFalse(isValidB)
        Assert.assertFalse(isValidC)
        Assert.assertFalse(isValidD)
        Assert.assertFalse(isValidE)

        val refuelWithMasterUidEmpty = Refuel(masterUid = "", id = "2", truckId = "3", driverId = "4", travelId = "5")
        val refuelWithIdEmpty = Refuel(masterUid = "1", id = "", truckId = "3", driverId = "4", travelId = "5")
        val refuelWithTruckIdEmpty = Refuel(masterUid = "1", id = "2", truckId = "", driverId = "4", travelId = "5")
        val refuelWithDriverIdEmpty = Refuel(masterUid = "1", id = "2", truckId = "3", driverId = "", travelId = "5")
        val refuelWithTravelIdEmpty= Refuel(masterUid = "1", id = "2", truckId = "3", driverId = "4", travelId = "")
        val isValidF = refuelWithMasterUidEmpty.validateIds()
        val isValidG = refuelWithIdEmpty.validateIds()
        val isValidH = refuelWithTruckIdEmpty.validateIds()
        val isValidI = refuelWithDriverIdEmpty.validateIds()
        val isValidJ = refuelWithTravelIdEmpty.validateIds()
        Assert.assertFalse(isValidF)
        Assert.assertFalse(isValidG)
        Assert.assertFalse(isValidH)
        Assert.assertFalse(isValidI)
        Assert.assertFalse(isValidJ)
    }



}