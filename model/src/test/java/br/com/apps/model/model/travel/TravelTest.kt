package br.com.apps.model.model.travel

import br.com.apps.model.exceptions.DateOrderException
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.OdometerOrderException
import br.com.apps.model.model.travel.Travel.Companion.merge
import br.com.apps.model.test_cases.sampleFreight
import br.com.apps.model.test_cases.sampleOutlay
import br.com.apps.model.test_cases.sampleRefuel
import br.com.apps.model.test_cases.sampleTravel
import br.com.apps.model.test_cases.sampleTravelAid
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.security.InvalidParameterException
import java.time.LocalDateTime

class TravelTest {

    private lateinit var travel: Travel

    @Before
    fun setup() {
        travel = sampleTravel()
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge travels to freights`() {
        val travels = listOf(
            sampleTravel(),
            sampleTravel().copy(id = "travelId2")
        )
        val freights = listOf(
            sampleFreight(),
            sampleFreight().copy(travelId = "travelId2", id = "freightId2")
        )

        travels.merge(nFreights = freights)

        val expected = "freightId1"
        val actual = travels[0].freights[0].id
        assertEquals(expected, actual)

        val expectedB = "freightId2"
        val actualB = travels[1].freights[0].id
        assertEquals(expectedB, actualB)

    }

    @Test
    fun `should merge travels to refuels`() {
        val travels = listOf(
            sampleTravel(),
            sampleTravel().copy(id = "travelId2")
        )
        val refuels = listOf(
            sampleRefuel(),
            sampleRefuel().copy(travelId = "travelId2", id = "refuelId2")
        )

        travels.merge(nRefuels = refuels)

        val expected = "refuelId1"
        val actual = travels[0].refuels[0].id
        assertEquals(expected, actual)

        val expectedB = "refuelId2"
        val actualB = travels[1].refuels[0].id
        assertEquals(expectedB, actualB)

    }

    @Test
    fun `should merge travels to outlays`() {
        val travels = listOf(
            sampleTravel(),
            sampleTravel().copy(id = "travelId2")
        )
        val outlays = listOf(
            sampleOutlay(),
            sampleOutlay().copy(travelId = "travelId2", id = "outlayId2")
        )

        travels.merge(nOutlays = outlays)

        val expected = "outlayId1"
        val actual = travels[0].outlays[0].id
        assertEquals(expected, actual)

        val expectedB = "outlayId2"
        val actualB = travels[1].outlays[0].id
        assertEquals(expectedB, actualB)

    }

    @Test
    fun `should merge travels to aids`() {
        val travels = listOf(
            sampleTravel(),
            sampleTravel().copy(id = "travelId2")
        )
        val aids = listOf(
            sampleTravelAid(),
            sampleTravelAid().copy(travelId = "travelId2", id = "travelAidId2")
        )

        travels.merge(nAids = aids)

        val expected = "travelAidId1"
        val actual = travels[0].aids[0].id
        assertEquals(expected, actual)

        val expectedB = "travelAidId2"
        val actualB = travels[1].aids[0].id
        assertEquals(expectedB, actualB)

    }

    //---------------------------------------------------------------------------------------------//
    // getListSize()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the size of the list for freights`() {
        addFreights()
        val freightSize = travel.getListSize(Travel.FREIGHT)
        assertEquals(2, freightSize)
    }

    @Test
    fun `should return the size of the list for outlay`() {
        addOutlays()
        val expendSize = travel.getListSize(Travel.OUTLAY)
        assertEquals(2, expendSize)
    }

    @Test
    fun `should return the size of the list for refuels`() {
        addRefuels()
        val refuelSize = travel.getListSize(Travel.REFUEL)
        assertEquals(2, refuelSize)
    }

    @Test
    fun `should return the size of the list for aids`() {
        addAids()
        val aidSize = travel.getListSize(Travel.AID)
        assertEquals(2, aidSize)
    }

    @Test
    fun `should throw exception when the int tag is wrong for list size`() {
        assertThrows(InvalidParameterException::class.java) {
            travel.getListSize(999)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getListOfIdsForList()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the freight list of ids for a selected item`() {
        addFreights()
        val freightIds = travel.getListOfIdsForList(Travel.FREIGHT)
        assertEquals(listOf("freightId1", "freightId2"), freightIds)
    }

    @Test
    fun `should return the refuel list of ids for a selected item`() {
        addRefuels()
        val refuelIds = travel.getListOfIdsForList(Travel.REFUEL)
        assertEquals(listOf("refuelId1", "refuelId2"), refuelIds)
    }

    @Test
    fun `should return the outlay list of ids for a selected item`() {
        addOutlays()

        val expected = listOf("outlayId1", "outlayId2")
        val actual = travel.getListOfIdsForList(Travel.OUTLAY)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the aid list of ids for a selected item`() {
        addAids()

        val expected = listOf("travelAidId1", "travelAidId2")
        val actual = travel.getListOfIdsForList(Travel.AID)

        assertEquals(expected, actual)
    }

    @Test
    fun `should throw exception when the int tag is wrong for list of ids`() {
        assertThrows(InvalidParameterException::class.java) {
            travel.getListOfIdsForList(999)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getCommissionValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the commission value when the freight list is good`() {
        addFreights()

        val expected = BigDecimal("2000.00")
        val commissionValue = travel.getCommissionValue()

        assertEquals(expected, commissionValue)
    }

    //---------------------------------------------------------------------------------------------//
    // getDifferenceBetweenInitialAndFinalOdometerMeasure()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the difference between initial and final odometer measure`() {
        val travelA = travel.copy(
            initialOdometerMeasurement = BigDecimal("10000.00"),
            finalOdometerMeasurement = BigDecimal("15000.00")
        )

        val expected = BigDecimal("5000.00")
        val actual = travelA.getDifferenceBetweenInitialAndFinalOdometerMeasure()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero when any odometer measure is null`() {
        val difference = travel.getDifferenceBetweenInitialAndFinalOdometerMeasure()
        assertEquals(BigDecimal.ZERO, difference)
    }

    //---------------------------------------------------------------------------------------------//
    // getListTotalValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the sum of values for the list tag - Freights`() {
        addFreights()

        val expected = BigDecimal("20000.00")
        val actual = travel.getListTotalValue(Travel.FREIGHT)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the sum of values for the list tag - Refuels`() {
        addRefuels()

        val expected = BigDecimal("8000.00")
        val actual = travel.getListTotalValue(Travel.REFUEL)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the sum of values for the list tag - Outlay`() {
        addOutlays()

        val expected = BigDecimal("1000.00")
        val expendSum = travel.getListTotalValue(Travel.OUTLAY)

        assertEquals(expected, expendSum)
    }

    @Test
    fun `should return the sum of values for the aid list tag - Aid`() {
        addAids()

        val expected = BigDecimal("200.00")
        val aidSum = travel.getListTotalValue(Travel.AID)

        assertEquals(expected, aidSum)
    }

    @Test
    fun `should return zero when the list is empty - Freights`() {
        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getListTotalValue(Travel.FREIGHT)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero when the list is empty - Refuels`() {
        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getListTotalValue(Travel.REFUEL)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero when the list is empty - Expends`() {
        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getListTotalValue(Travel.OUTLAY)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero when the list is empty - Aid`() {
        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getListTotalValue(Travel.AID)
        assertEquals(expected, actual)
    }

    @Test
    fun `should throw exception when the int tag is wrong for sum of values`() {
        assertThrows(InvalidParameterException::class.java) {
            travel.getListTotalValue(999)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getLiquidValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the liquid value for this travel when the values are good`() {
        addFreights()
        addRefuels()
        addAids()
        addOutlays()

        val expected = BigDecimal("9000.00")
        val actual = travel.getLiquidValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the liquid value when the value is negative`() {
        travel.addFreight(sampleFreight().copy(value = BigDecimal("8000.00")))
        addRefuels()
        addAids()
        addOutlays()

        val expected = BigDecimal("-1800.00")
        val actual = travel.getLiquidValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the liquid value with empty freight list`() {
        addOutlays()
        addRefuels()

        val expected = BigDecimal("-9000.00")
        val actual = travel.getLiquidValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the liquid value with empty refuel list`() {
        addFreights()
        addOutlays()

        val expected = BigDecimal("17000.00")
        val actual = travel.getLiquidValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the liquid value with empty outlay list`() {
        addFreights()
        addRefuels()

        val expected = BigDecimal("10000.00")
        val actual = travel.getLiquidValue()

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // getTravelAuthenticationPercent()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the auth percent when values are good`() {
        val expected = 100.0
        travel.addFreight(sampleFreight().copy(id = "freightId1", isValid = true))
        travel.addRefuel(sampleRefuel().copy(id = "refuelId1", isValid = true))
        travel.addOutlay(sampleOutlay().copy(id = "outlayId1", isValid = true))

        val authPercTotal = travel.getTravelAuthenticationPercent()

        assertEquals(expected, authPercTotal, 0.0)
    }

    @Test
    fun `should return the auth percent when freight list is null or empty`() {
        val expected = 100.0
        travel.addRefuel(sampleRefuel().copy(id = "refuelId1", isValid = true))
        travel.addOutlay(sampleOutlay().copy(id = "outlayId1", isValid = true))

        val authPercTotal = travel.getTravelAuthenticationPercent()

        assertEquals(expected, authPercTotal, 0.0)
    }

    @Test
    fun `should return the auth percent when refuel list is null or empty`() {
        val expected = 100.0
        travel.addFreight(sampleFreight().copy(id = "freightId1", isValid = true))
        travel.addRefuel(sampleRefuel().copy(id = "refuelId1", isValid = true))

        val authPercTotal = travel.getTravelAuthenticationPercent()

        assertEquals(expected, authPercTotal, 0.0)
    }

    //---------------------------------------------------------------------------------------------//
    // isReadyToBeFinished()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true when the travel fully valid`() {
        travel.addFreight(sampleFreight().copy(id = "freightId1", isValid = true))
        travel.addRefuel(sampleRefuel().copy(id = "refuelId1", isValid = true))
        travel.addOutlay(sampleOutlay().copy(id = "outlayId1", isValid = true))

        assertTrue(travel.isReadyToBeFinished())
    }

    @Test
    fun `should return false when the travel is not fully valid`() {
        travel.addFreight(sampleFreight().copy(id = "freightId1", isValid = true))
        travel.addRefuel(sampleRefuel().copy(id = "refuelId1", isValid = true))
        travel.addOutlay(sampleOutlay().copy(id = "outlayId1", isValid = false))

        assertFalse(travel.isReadyToBeFinished())
    }

    @Test
    fun `should return false when the travel is empty`() {
        assertFalse(travel.isReadyToBeFinished())
    }

    //---------------------------------------------------------------------------------------------//
    // isEmptyTravel()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true when the travel has no registered itens`() {
        assertTrue(travel.isEmptyTravel())
    }

    @Test
    fun `should return false when there is at least one freight`() {
        addFreights()
        assertFalse(travel.isEmptyTravel())
    }

    @Test
    fun `should return false when there is at least one refuel`() {
        addRefuels()
        assertFalse(travel.isEmptyTravel())
    }

    @Test
    fun `should return false when there is at least one outlay`() {
        addOutlays()
        assertFalse(travel.isEmptyTravel())
    }

    @Test
    fun `should return false when there is at least one aid`() {
        addAids()
        assertFalse(travel.isEmptyTravel())
    }

    //---------------------------------------------------------------------------------------------//
    // validateForSaving()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw odometerOrderException when odometer measures are in wrong order`() {
        val travelA = travel.copy(
            initialOdometerMeasurement = BigDecimal("10.00"),
            finalOdometerMeasurement = BigDecimal("5.00"),
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 2, 0, 0),
            isFinished = true
        ).apply {
            addFreight(sampleFreight().copy(isValid = true))
            addRefuel(sampleRefuel().copy(isValid = true))
            addOutlay(sampleOutlay().copy(isValid = true))
            addAid(sampleTravelAid().copy(isValid = true))
        }

        assertThrows(OdometerOrderException::class.java) {
            travelA.validateForSaving()
        }

    }

    @Test
    fun `should throw dateOrderException when dates are in wrong order`() {
        val travelA = travel.copy(
            initialDate = LocalDateTime.of(2022, 1, 2, 0, 0),
            finalDate = LocalDateTime.of(2022, 1, 1, 0, 0),
            finalOdometerMeasurement = BigDecimal("200.00"),
            isFinished = true
        ).apply {
            addFreight(sampleFreight().copy(isValid = true))
            addRefuel(sampleRefuel().copy(isValid = true))
            addOutlay(sampleOutlay().copy(isValid = true))
            addAid(sampleTravelAid().copy(isValid = true))
        }

        assertThrows(DateOrderException::class.java) {
            travelA.validateForSaving()
        }

    }

    @Test
    fun `should throw dateOrderException when final date is null`() {
        val travelA = travel.copy(
            initialDate = LocalDateTime.of(2022, 1, 2, 12, 0),
            finalDate = null,
            isFinished = true
        ).apply {
            addFreight(sampleFreight().copy(isValid = true))
            addRefuel(sampleRefuel().copy(isValid = true))
            addOutlay(sampleOutlay().copy(isValid = true))
        }

        assertThrows(NullPointerException::class.java) {
            travelA.validateForSaving()
        }

    }

    @Test
    fun `should throw exception when there is any invalid freight`() {
        addFreights()
        assertThrows(InvalidParameterException::class.java) {
            travel.validateForSaving()
        }
    }

    @Test
    fun `should throw exception when the freight list is empty`() {
        assertThrows(EmptyDataException::class.java) {
            travel.validateForSaving()
        }
    }

    @Test
    fun `should throw exception when there is any invalid refuel`() {
        val freightA = sampleFreight().copy(isValid = true)
        travel.addFreight(freightA)
        addRefuels()

        assertThrows(InvalidParameterException::class.java) {
            travel.validateForSaving()
        }

    }

    @Test
    fun `should throw exception when there is any invalid outlay`() {
        val freightA = sampleFreight().copy(isValid = true)
        travel.addFreight(freightA)

        val refuelA = sampleRefuel().copy(isValid = true)
        travel.addRefuel(refuelA)

        addOutlays()

        assertThrows(InvalidParameterException::class.java) {
            travel.validateForSaving()
        }

    }

    //---------------------------------------------------------------------------------------------//
    // isDeletable()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true when travel can be deleted`() {
        val travelA = sampleTravel().copy(isFinished = false)
            .apply {
                addFreight(sampleFreight().copy(isValid = false))
                addRefuel(sampleRefuel().copy(isValid = false))
                addOutlay(sampleOutlay().copy(isValid = false))
                addAid(sampleTravelAid().copy(isValid = false))
            }

        assertTrue(travelA.isDeletable())

    }

    @Test
    fun `should return false when travel cannot be deleted because is the travel finished`() {
        val travelB = sampleTravel().copy(isFinished = true)
        assertFalse(travelB.isDeletable())
    }

    @Test
    fun `should return false when freights cannot be deleted`() {
        val freightB = sampleFreight().copy(isValid = true)
        travel.addFreight(freightB)
        assertFalse(travel.isDeletable())
    }

    @Test
    fun `should return false when refuels cannot be deleted`() {
        val refuelB = sampleRefuel().copy(isValid = true)
        travel.addRefuel(refuelB)
        assertFalse(travel.isDeletable())
    }


    @Test
    fun `should return false when outlay cannot be deleted`() {
        val outlayB = sampleOutlay().copy(isValid = true)
        travel.addOutlay(outlayB)
        assertFalse(travel.isDeletable())
    }

    @Test
    fun `should return false when aids cannot be deleted`() {
        val aidA = sampleTravelAid().copy(isValid = true)
        travel.addAid(aidA)

        assertFalse(travel.isDeletable())
    }

    //---------------------------------------------------------------------------------------------//
    // getProfitPercent()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the profit percent when the values are good`() {
        addFreights()
        addRefuels()
        addOutlays()

        val expected = BigDecimal("45.00")

        val actual = travel.getProfitPercent()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero profit percent when the freight list is empty`() {
        addOutlays()
        addRefuels()

        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getProfitPercent()

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // getFuelAverage()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the fuel average when values are good`() {
        val travelA = sampleTravel().copy(
            initialOdometerMeasurement = BigDecimal.ZERO,
            finalOdometerMeasurement = BigDecimal("1000.00")
        ).apply {
            addRefuel(sampleRefuel().copy(id = "refuelId1", amountLiters = BigDecimal("200.00")))
            addRefuel(sampleRefuel().copy(id = "refuelId2", amountLiters = BigDecimal("200.00")))
        }

        val expected = BigDecimal("2.50")

        val actual = travelA.getFuelAverage()

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // shouldConsiderAverage()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true if the last refuel had a complete refuel`() {
        addRefuels()
        assertTrue(travel.shouldConsiderAverage())
    }

    @Test
    fun `should return false if the last refuel is not complete refuel`() {
        val refuelA = sampleRefuel().copy(isCompleteRefuel = false)
        travel.addRefuel(refuelA)

        val expected = travel.shouldConsiderAverage()

        assertFalse(expected)
    }

    @Test
    fun `should return false if the refuel list is empty`() {
        assertFalse(travel.shouldConsiderAverage())
    }


    //---------------------

    private fun addFreights() {
        val freightA = sampleFreight()
        val freightB = sampleFreight().copy(id = "freightId2")

        travel.addFreight(freightA)
        travel.addFreight(freightB)
    }

    private fun addRefuels() {
        val refuelA = sampleRefuel()
        val refuelB = sampleRefuel().copy(id = "refuelId2", odometerMeasure = BigDecimal("20.00"))

        travel.addRefuel(refuelA)
        travel.addRefuel(refuelB)
    }

    private fun addOutlays() {
        val outlayA = sampleOutlay()
        val outlayB = sampleOutlay().copy(id = "outlayId2")

        travel.addOutlay(outlayA)
        travel.addOutlay(outlayB)
    }

    private fun addAids() {
        val aidA = sampleTravelAid()
        val aidB = sampleTravelAid().copy(id = "travelAidId2")

        travel.addAid(aidA)
        travel.addAid(aidB)
    }

}