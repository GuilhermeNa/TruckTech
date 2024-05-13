package br.com.apps.model.model

import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class TravelTest {

    private val travel = Travel(
        masterUid = "1",
        id = "2",
        truckId = "3",
        driverId = "4",

        isFinished = false,
        initialDate = LocalDateTime.of(2022, 1, 1, 12, 0),
        finalDate = LocalDateTime.of(2022, 1, 5, 12, 0),
        initialOdometerMeasurement = BigDecimal("10"),
        finalOdometerMeasurement = BigDecimal("50"),
        freightsList = listOf(Freight(id = "1"), Freight(id = "2")),
        refuelsList = listOf(Refuel(id = "3"), Refuel(id = "4"), Refuel(id = "5")),
        expendsList = listOf(Expend(id = "6"))
    )

    @Test
    fun `should return the size of the list`() {
        val freightSize = travel.getListSize(Travel.FREIGHT)
        val expendSize = travel.getListSize(Travel.EXPEND)
        val refuelSize = travel.getListSize(Travel.REFUEL)

        Assert.assertEquals(2, freightSize)
        Assert.assertEquals(1, expendSize)
        Assert.assertEquals(3, refuelSize)
    }

    @Test
    fun `should return zero when the list is null`() {
        travel.apply {
            expendsList = null
            refuelsList = null
            freightsList = emptyList()
        }

        val freightSize = travel.getListSize(Travel.FREIGHT)
        val expendSize = travel.getListSize(Travel.EXPEND)
        val refuelSize = travel.getListSize(Travel.REFUEL)

        Assert.assertEquals(0, freightSize)
        Assert.assertEquals(0, expendSize)
        Assert.assertEquals(0, refuelSize)
    }

    @Test
    fun `should return size zero when the int tag is wrong`() {
        val wrongSize = travel.getListSize(999)
        Assert.assertEquals(0, wrongSize)
    }

    @Test
    fun `should return the list of ids for a selected item`() {
        val freightIds = travel.getListOfIdsForList(Travel.FREIGHT)
        val refuelIds = travel.getListOfIdsForList(Travel.REFUEL)
        val expendIds = travel.getListOfIdsForList(Travel.EXPEND)

        Assert.assertEquals(listOf("1", "2"), freightIds)
        Assert.assertEquals(listOf("3", "4", "5"), refuelIds)
        Assert.assertEquals(listOf("6"), expendIds)
    }

    @Test
    fun `should return an empty list when the list is null`() {
        travel.apply {
            refuelsList = null
            freightsList = null
            expendsList = emptyList()
        }

        val freightList = travel.getListOfIdsForList(Travel.FREIGHT)
        val refuelList = travel.getListOfIdsForList(Travel.REFUEL)
        val expendList = travel.getListOfIdsForList(Travel.EXPEND)

        Assert.assertEquals(listOf<String>(), freightList)
        Assert.assertEquals(listOf<String>(), refuelList)
        Assert.assertEquals(listOf<String>(), expendList)
    }

    @Test
    fun `should return the difference between initial and final odometer measure`() {
        val difference = travel.getDifferenceBetweenInitialAndFinalOdometerMeasure()
        Assert.assertEquals(BigDecimal("40"), difference)
    }

    @Test
    fun `should return zero when any odometer measure is null`() {
        travel.apply {
            finalOdometerMeasurement = null
        }
        val difference = travel.getDifferenceBetweenInitialAndFinalOdometerMeasure()
        Assert.assertEquals(BigDecimal.ZERO, difference)

        travel.apply {
            initialOdometerMeasurement = null
            finalOdometerMeasurement = BigDecimal("50")
        }
        val sDifference = travel.getDifferenceBetweenInitialAndFinalOdometerMeasure()
        Assert.assertEquals(BigDecimal.ZERO, sDifference)
    }

    @Test
    fun `should return true if initial date is before final date`() {
        val isDateGood = travel.checkDatesOrder()
        Assert.assertEquals(true, isDateGood)
    }

    @Test
    fun `should return false if initial date is after final date or any is null`() {
        val travelWithWrongDates = Travel(
            initialDate = LocalDateTime.of(2022, 1, 2, 12, 0),
            finalDate = LocalDateTime.of(2022, 1, 1, 12, 0)
        )
        val travelWithNullDates = Travel(
            initialDate = null,
            finalDate = null
        )
        val travelWithNullInitialDate = Travel(
            initialDate = null,
            finalDate = LocalDateTime.of(2022, 1, 2, 12, 0)
        )
        val travelWithNullFinalDate = Travel(
            initialDate = LocalDateTime.of(2022, 1, 2, 12, 0),
            finalDate = null
        )

        val result1 = travelWithWrongDates.checkDatesOrder()
        val result2 = travelWithNullDates.checkDatesOrder()
        val result3 = travelWithNullInitialDate.checkDatesOrder()
        val result4 = travelWithNullFinalDate.checkDatesOrder()

        Assert.assertEquals(false, result1)
        Assert.assertEquals(false, result2)
        Assert.assertEquals(false, result3)
        Assert.assertEquals(false, result4)
    }

    @Test
    fun `should return true if there is any duplicated item in list`() {
        val isItemDuplicated = travel.checkForDuplicatedItems()
        Assert.assertEquals(false, isItemDuplicated)
    }

    @Test
    fun `should return false if there is no duplicated item in list`() {
        val freightDuplicated = Travel(
            freightsList = listOf(Freight(id = "1"), Freight(id = "1"))
        )
        val refuelDuplicated = Travel(
            refuelsList = listOf(Refuel(id = "3"), Refuel(id = "3"))
        )
        val expendDuplicated = Travel(
            expendsList = listOf(Expend(id = "6"), Expend(id = "6"))
        )

        val isFreightDuplicated = freightDuplicated.checkForDuplicatedItems()
        val isRefuelDuplicated = refuelDuplicated.checkForDuplicatedItems()
        val isExpendDuplicated = expendDuplicated.checkForDuplicatedItems()

        Assert.assertEquals(true, isFreightDuplicated)
        Assert.assertEquals(true, isRefuelDuplicated)
        Assert.assertEquals(true, isExpendDuplicated)
    }

    @Test
    fun `should return false if there is a null list or item`() {
        val freightWithNullItem = Travel(
            freightsList = listOf(Freight(id = null), Freight(id = null))
        )
        val refuelWithNullItem = Travel(
            refuelsList = listOf(Refuel(id = null), Refuel(id = null))
        )
        val expendWithNullItem = Travel(
            expendsList = listOf(Expend(id = null), Expend(id = null))
        )
        val freightListNull = Travel(
            freightsList = null
        )
        val refuelListNull = Travel(
            refuelsList = null
        )
        val expendListNull = Travel(
            expendsList = null
        )

        val isFreightWithNullItem = freightWithNullItem.checkForDuplicatedItems()
        val isRefuelWithNullItem = refuelWithNullItem.checkForDuplicatedItems()
        val isExpendWithNullItem = expendWithNullItem.checkForDuplicatedItems()
        val isFreightListNull = freightListNull.checkForDuplicatedItems()
        val isRefuelListNull = refuelListNull.checkForDuplicatedItems()
        val isExpendListNull = expendListNull.checkForDuplicatedItems()

        Assert.assertEquals(false, isFreightWithNullItem)
        Assert.assertEquals(false, isRefuelWithNullItem)
        Assert.assertEquals(false, isExpendWithNullItem)
        Assert.assertEquals(false, isFreightListNull)
        Assert.assertEquals(false, isRefuelListNull)
        Assert.assertEquals(false, isExpendListNull)
    }

    @Test
    fun `should return true when all ids are valid`() {
        val isIdsValid = travel.validateIds()
        Assert.assertTrue(isIdsValid)
    }

    @Test
    fun `should return false when any id is null or empty`() {
        val travelWithMasterUidNull = Travel(masterUid = null, id = "2", truckId = "3", driverId = "4")
        val travelWithIdNull = Travel(masterUid = "1", id = null, truckId = "3", driverId = "4")
        val travelWithTruckIdNull = Travel(masterUid = "1", id = "2", truckId = null, driverId = "4")
        val travelWithDriverIdNull = Travel(masterUid = "1", id = "2", truckId = "3", driverId = null)
        val isValidA = travelWithMasterUidNull.validateIds()
        val isValidB = travelWithIdNull.validateIds()
        val isValidC = travelWithTruckIdNull.validateIds()
        val isValidD = travelWithDriverIdNull.validateIds()
        Assert.assertFalse(isValidA)
        Assert.assertFalse(isValidB)
        Assert.assertFalse(isValidC)
        Assert.assertFalse(isValidD)


        val travelWithMasterUidEmpty = Travel(masterUid = "", id = "2", truckId = "3", driverId = "4")
        val travelWithIdEmpty = Travel(masterUid = "1", id = "", truckId = "3", driverId = "4")
        val travelWithTruckIdEmpty = Travel(masterUid = "1", id = "2", truckId = "", driverId = "4")
        val travelWithDriverIdEmpty = Travel(masterUid = "1", id = "2", truckId = "3", driverId = "")
        val isValidE = travelWithMasterUidEmpty.validateIds()
        val isValidF = travelWithIdEmpty.validateIds()
        val isValidG = travelWithTruckIdEmpty.validateIds()
        val isValidH = travelWithDriverIdEmpty.validateIds()
        Assert.assertFalse(isValidE)
        Assert.assertFalse(isValidF)
        Assert.assertFalse(isValidG)
        Assert.assertFalse(isValidH)
    }

}