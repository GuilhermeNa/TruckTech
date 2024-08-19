package br.com.apps.model.model.travel

import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.invalid.InvalidDateException
import br.com.apps.model.model.travel.Travel.Companion.AID
import br.com.apps.model.model.travel.Travel.Companion.FREIGHT
import br.com.apps.model.model.travel.Travel.Companion.OUTLAY
import br.com.apps.model.model.travel.Travel.Companion.REFUEL
import br.com.apps.model.model.travel.Travel.Companion.getProfitPercent
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
    // addAllFreights()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add all freights`() {
        travel.addAllFreights(listOf(sampleFreight()))

        val expected = 1
        val actual = travel.getSizeOf(FREIGHT)

        assertEquals(expected, actual)
    }

    @Test
    fun `should not repeat freights with same id`() {
        travel.addAllFreights(
            listOf(
                sampleFreight(),
                sampleFreight()
            )
        )

        val expected = 1
        val actual = travel.getSizeOf(FREIGHT)

        assertEquals(expected, actual)
    }

    @Test
    fun `should clear the previous and add all freights`() {
        travel.addFreight(sampleFreight())

        travel.addAllFreights(
            listOf(
                sampleFreight(),
                sampleFreight().copy(id = "freightId2")
            )
        )

        val expected = 2
        val actual = travel.getSizeOf(FREIGHT)

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // addAllRefuels()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add all refuels`() {
        travel.addAllRefuels(listOf(sampleRefuel()))

        val expected = 1
        val actual = travel.getSizeOf(REFUEL)

        assertEquals(expected, actual)
    }

    @Test
    fun `should not repeat refuels with same id`() {
        travel.addAllRefuels(
            listOf(
                sampleRefuel(),
                sampleRefuel()
            )
        )

        val expected = 1
        val actual = travel.getSizeOf(REFUEL)

        assertEquals(expected, actual)
    }

    @Test
    fun `should clear the previous and add all refuels`() {
        travel.addRefuel(sampleRefuel())

        travel.addAllRefuels(
            listOf(
                sampleRefuel(),
                sampleRefuel().copy(id = "refuelId2")
            )
        )

        val expected = 2
        val actual = travel.getSizeOf(REFUEL)

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // addAllOutlays()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add all outlays`() {
        travel.addAllOutlays(listOf(sampleOutlay()))

        val expected = 1
        val actual = travel.getSizeOf(OUTLAY)

        assertEquals(expected, actual)
    }

    @Test
    fun `should not repeat outlays with same id`() {
        travel.addAllOutlays(
            listOf(
                sampleOutlay(),
                sampleOutlay()
            )
        )

        val expected = 1
        val actual = travel.getSizeOf(OUTLAY)

        assertEquals(expected, actual)
    }

    @Test
    fun `should clear the previous and add all outlays`() {
        travel.addOutlay(sampleOutlay())

        travel.addAllOutlays(
            listOf(
                sampleOutlay(),
                sampleOutlay().copy(id = "outlayId2")
            )
        )

        val expected = 2
        val actual = travel.getSizeOf(OUTLAY)

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // addAllOutlays()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add all aids`() {
        travel.addAllAids(listOf(sampleTravelAid()))

        val expected = 1
        val actual = travel.getSizeOf(AID)

        assertEquals(expected, actual)
    }

    @Test
    fun `should not repeat aids with same id`() {
        travel.addAllAids(
            listOf(
                sampleTravelAid(),
                sampleTravelAid()
            )
        )

        val expected = 1
        val actual = travel.getSizeOf(AID)

        assertEquals(expected, actual)
    }

    @Test
    fun `should clear the previous and add all aids`() {
        travel.addAid(sampleTravelAid())

        travel.addAllAids(
            listOf(
                sampleTravelAid(),
                sampleTravelAid().copy(id = "travelAid2")
            )
        )

        val expected = 2
        val actual = travel.getSizeOf(AID)

        assertEquals(expected, actual)
    }

    //---------------------------------------------------------------------------------------------//
    // getSizeOf()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the size of the list for freights`() {
        addFreights()
        val freightSize = travel.getSizeOf(Travel.FREIGHT)
        assertEquals(2, freightSize)
    }

    @Test
    fun `should return the size of the list for outlay`() {
        addOutlays()
        val expendSize = travel.getSizeOf(Travel.OUTLAY)
        assertEquals(2, expendSize)
    }

    @Test
    fun `should return the size of the list for refuels`() {
        addRefuels()
        val refuelSize = travel.getSizeOf(Travel.REFUEL)
        assertEquals(2, refuelSize)
    }

    @Test
    fun `should return the size of the list for aids`() {
        addAids()
        val aidSize = travel.getSizeOf(Travel.AID)
        assertEquals(2, aidSize)
    }

    @Test
    fun `should throw exception when the int tag is wrong for list size`() {
        assertThrows(InvalidParameterException::class.java) {
            travel.getSizeOf(999)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getIdsOf()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the freight list of ids for a selected item`() {
        addFreights()
        val freightIds = travel.getIdsOf(Travel.FREIGHT)
        assertEquals(listOf("freightId1", "freightId2"), freightIds)
    }

    @Test
    fun `should return the refuel list of ids for a selected item`() {
        addRefuels()
        val refuelIds = travel.getIdsOf(Travel.REFUEL)
        assertEquals(listOf("refuelId1", "refuelId2"), refuelIds)
    }

    @Test
    fun `should return the outlay list of ids for a selected item`() {
        addOutlays()

        val expected = listOf("outlayId1", "outlayId2")
        val actual = travel.getIdsOf(Travel.OUTLAY)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the aid list of ids for a selected item`() {
        addAids()

        val expected = listOf("travelAidId1", "travelAidId2")
        val actual = travel.getIdsOf(Travel.AID)

        assertEquals(expected, actual)
    }

    @Test
    fun `should throw exception when the int tag is wrong for list of ids`() {
        assertThrows(InvalidParameterException::class.java) {
            travel.getIdsOf(999)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getCommission()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the commission value when the freight list is good`() {
        addFreights()

        val expected = BigDecimal("2000.00")
        val commissionValue = travel.getCommission()

        assertEquals(expected, commissionValue)
    }

    //---------------------------------------------------------------------------------------------//
    // getTraveledDistance()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the difference between initial and final odometer measure`() {
        val travelA = travel.copy(
            initialOdometer = BigDecimal("10000.00"),
            finalOdometer = BigDecimal("15000.00")
        )

        val expected = BigDecimal("5000.00")
        val actual = travelA.getTraveledDistance()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero when any odometer measure is null`() {
        val difference = travel.getTraveledDistance()
        assertEquals(BigDecimal.ZERO, difference)
    }

    //---------------------------------------------------------------------------------------------//
    // getValueOf()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the sum of values for the list tag - Freights`() {
        addFreights()

        val expected = BigDecimal("20000.00")
        val actual = travel.getValueOf(Travel.FREIGHT)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the sum of values for the list tag - Refuels`() {
        addRefuels()

        val expected = BigDecimal("8000.00")
        val actual = travel.getValueOf(Travel.REFUEL)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the sum of values for the list tag - Outlay`() {
        addOutlays()

        val expected = BigDecimal("1000.00")
        val expendSum = travel.getValueOf(Travel.OUTLAY)

        assertEquals(expected, expendSum)
    }

    @Test
    fun `should return the sum of values for the aid list tag - Aid`() {
        addAids()

        val expected = BigDecimal("200.00")
        val aidSum = travel.getValueOf(Travel.AID)

        assertEquals(expected, aidSum)
    }

    @Test
    fun `should return zero when the list is empty - Freights`() {
        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getValueOf(Travel.FREIGHT)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero when the list is empty - Refuels`() {
        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getValueOf(Travel.REFUEL)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero when the list is empty - Expends`() {
        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getValueOf(Travel.OUTLAY)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return zero when the list is empty - Aid`() {
        val expected = BigDecimal.ZERO.setScale(2)
        val actual = travel.getValueOf(Travel.AID)
        assertEquals(expected, actual)
    }

    @Test
    fun `should throw exception when the int tag is wrong for sum of values`() {
        assertThrows(InvalidParameterException::class.java) {
            travel.getValueOf(999)
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

        val authPercTotal = travel.getAuthPercent()

        assertEquals(expected, authPercTotal, 0.0)
    }

    @Test
    fun `should return the auth percent when freight list is null or empty`() {
        val expected = 100.0
        travel.addRefuel(sampleRefuel().copy(id = "refuelId1", isValid = true))
        travel.addOutlay(sampleOutlay().copy(id = "outlayId1", isValid = true))

        val authPercTotal = travel.getAuthPercent()

        assertEquals(expected, authPercTotal, 0.0)
    }

    @Test
    fun `should return the auth percent when refuel list is null or empty`() {
        val expected = 100.0
        travel.addFreight(sampleFreight().copy(id = "freightId1", isValid = true))
        travel.addRefuel(sampleRefuel().copy(id = "refuelId1", isValid = true))

        val authPercTotal = travel.getAuthPercent()

        assertEquals(expected, authPercTotal, 0.0)
    }

    //---------------------------------------------------------------------------------------------//
    // isReadyToFinalize()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true when travel is ready to finalize`() {
        travel.addFreight(sampleFreight().copy(isValid = true))
        travel.addRefuel(sampleRefuel().copy(isValid = true))
        travel.addOutlay(sampleOutlay().copy(isValid = true))
        travel.addAid(sampleTravelAid().copy(isValid = true))

        assertTrue(travel.isReadyToFinalize())
    }

    @Test
    fun `should return false when freight list is empty`() {
        travel.addRefuel(sampleRefuel().copy(isValid = true))
        travel.addOutlay(sampleOutlay().copy(isValid = true))
        travel.addAid(sampleTravelAid().copy(isValid = true))

        assertFalse(travel.isReadyToFinalize())
    }

    @Test
    fun `should return false when freight list is invalid`() {
        travel.addFreight(sampleFreight().copy(isValid = false))
        travel.addRefuel(sampleRefuel().copy(isValid = true))
        travel.addOutlay(sampleOutlay().copy(isValid = true))
        travel.addAid(sampleTravelAid().copy(isValid = true))

        assertFalse(travel.isReadyToFinalize())
    }

    @Test
    fun `should return false when refuel list is empty`() {
        travel.addFreight(sampleFreight().copy(isValid = true))
        travel.addOutlay(sampleOutlay().copy(isValid = true))
        travel.addAid(sampleTravelAid().copy(isValid = true))

        assertFalse(travel.isReadyToFinalize())
    }

    @Test
    fun `should return false when refuel list is invalid`() {
        travel.addFreight(sampleFreight().copy(isValid = true))
        travel.addRefuel(sampleRefuel().copy(isValid = false))
        travel.addOutlay(sampleOutlay().copy(isValid = true))
        travel.addAid(sampleTravelAid().copy(isValid = true))

        assertFalse(travel.isReadyToFinalize())
    }

    @Test
    fun `should return false when outlay list is empty`() {
        travel.addFreight(sampleFreight().copy(isValid = true))
        travel.addRefuel(sampleRefuel().copy(isValid = true))
        travel.addAid(sampleTravelAid().copy(isValid = true))

        assertFalse(travel.isReadyToFinalize())
    }

    @Test
    fun `should return false when outlay list is invalid`() {
        travel.addFreight(sampleFreight().copy(isValid = true))
        travel.addRefuel(sampleRefuel().copy(isValid = true))
        travel.addOutlay(sampleOutlay().copy(isValid = false))
        travel.addAid(sampleTravelAid().copy(isValid = true))

        assertFalse(travel.isReadyToFinalize())
    }

    @Test
    fun `should return false when aid list is invalid`() {
        travel.addFreight(sampleFreight().copy(isValid = true))
        travel.addRefuel(sampleRefuel().copy(isValid = true))
        travel.addOutlay(sampleOutlay().copy(isValid = true))
        travel.addAid(sampleTravelAid().copy(isValid = false))

        assertFalse(travel.isReadyToFinalize())
    }

    //---------------------------------------------------------------------------------------------//
    // isEmpty()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true when the travel has no registered itens`() {
        assertTrue(travel.isEmpty())
    }

    @Test
    fun `should return false when there is at least one freight`() {
        addFreights()
        assertFalse(travel.isEmpty())
    }

    @Test
    fun `should return false when there is at least one refuel`() {
        addRefuels()
        assertFalse(travel.isEmpty())
    }

    @Test
    fun `should return false when there is at least one outlay`() {
        addOutlays()
        assertFalse(travel.isEmpty())
    }

    @Test
    fun `should return false when there is at least one aid`() {
        addAids()
        assertFalse(travel.isEmpty())
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
            initialOdometer = BigDecimal.ZERO,
            finalOdometer = BigDecimal("1000.00")
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

    //---------------------------------------------------------------------------------------------//
    // addFreight()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add a freight when data is correct`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
        val freight = sampleFreight().copy(
            loadingDate = LocalDateTime.of(2024, 1, 2, 0, 0)
        )

        travelA.addFreight(freight)

        val actual = travelA.freights[0]

        assertEquals(freight, actual)
    }

    @Test
    fun `should add a freight when travel initialDate and freight loadingDate are equal`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
        val freight = sampleFreight().copy(
            loadingDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )

        travelA.addFreight(freight)

        val actual = travelA.freights[0]

        assertEquals(freight, actual)
    }

    @Test
    fun `should add a freight when travel finalDate and freight loadingDate are equal`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 10, 0, 0)
        )
        val freight = sampleFreight().copy(
            loadingDate = LocalDateTime.of(2024, 1, 10, 0, 0)
        )

        travelA.addFreight(freight)

        val actual = travelA.freights[0]

        assertEquals(freight, actual)
    }

    @Test
    fun `should throw InvalidDateException when freight loadingDate is before travel initialDate`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 2, 0, 0),
        )
        val freight = sampleFreight().copy(
            loadingDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )

        assertThrows(InvalidDateException::class.java) {
            travelA.addFreight(freight)
        }
    }

    @Test
    fun `should throw InvalidDateException when freight loadingDate is after travel initialDate`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 5, 0, 0)
        )
        val freight = sampleFreight().copy(
            loadingDate = LocalDateTime.of(2024, 1, 6, 0, 0)
        )

        assertThrows(InvalidDateException::class.java) {
            travelA.addFreight(freight)
        }
    }

    @Test
    fun `should throw DuplicatedItemsException when the freight id already exists in list`() {
        val freight = sampleFreight()
        travel.addFreight(freight)

        assertThrows(DuplicatedItemsException::class.java) {
            travel.addFreight(freight)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // addRefuel()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add a refuel when data is correct`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
        val refuel = sampleRefuel().copy(
            date = LocalDateTime.of(2024, 1, 2, 0, 0)
        )

        travelA.addRefuel(refuel)

        val actual = travelA.refuels[0]

        assertEquals(refuel, actual)
    }

    @Test
    fun `should add a refuel when travel initialDate and refuel date are equal`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
        val refuel = sampleRefuel().copy(
            date = LocalDateTime.of(2024, 1, 1, 0, 0)
        )

        travelA.addRefuel(refuel)

        val actual = travelA.refuels[0]

        assertEquals(refuel, actual)
    }

    @Test
    fun `should add a refuel when travel finalDate and refuel date are equal`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 10, 0, 0)
        )
        val refuel = sampleRefuel().copy(
            date = LocalDateTime.of(2024, 1, 10, 0, 0)
        )

        travelA.addRefuel(refuel)

        val actual = travelA.refuels[0]

        assertEquals(refuel, actual)
    }

    @Test
    fun `should throw InvalidDateException when refuel date is before travel initialDate`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 2, 0, 0),
        )
        val refuel = sampleRefuel().copy(
            date = LocalDateTime.of(2024, 1, 1, 0, 0)
        )

        assertThrows(InvalidDateException::class.java) {
            travelA.addRefuel(refuel)
        }
    }

    @Test
    fun `should throw InvalidDateException when refuel date is after travel initialDate`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 5, 0, 0)
        )
        val refuel = sampleRefuel().copy(
            date = LocalDateTime.of(2024, 1, 6, 0, 0)
        )

        assertThrows(InvalidDateException::class.java) {
            travelA.addRefuel(refuel)
        }
    }

    @Test
    fun `should throw DuplicatedItemsException when the refuel id already exists in list`() {
        val refuel = sampleRefuel()
        travel.addRefuel(refuel)

        assertThrows(DuplicatedItemsException::class.java) {
            travel.addRefuel(refuel)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // addOutlay()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add a outlay when data is correct`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
        val outlay = sampleOutlay().copy(
            date = LocalDateTime.of(2024, 1, 2, 0, 0)
        )

        travelA.addOutlay(outlay)

        val actual = travelA.outlays[0]

        assertEquals(outlay, actual)
    }

    @Test
    fun `should add a outlay when travel initialDate and outlay date are equal`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
        val outlay = sampleOutlay().copy(
            date = LocalDateTime.of(2024, 1, 1, 0, 0)
        )

        travelA.addOutlay(outlay)

        val actual = travelA.outlays[0]

        assertEquals(outlay, actual)
    }

    @Test
    fun `should add a outlay when travel finalDate and outlay date are equal`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 10, 0, 0)
        )
        val outlay = sampleOutlay().copy(
            date = LocalDateTime.of(2024, 1, 10, 0, 0)
        )

        travelA.addOutlay(outlay)

        val actual = travelA.outlays[0]

        assertEquals(outlay, actual)
    }

    @Test
    fun `should throw InvalidDateException when outlay date is before travel initialDate`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 2, 0, 0),
        )
        val outlay = sampleOutlay().copy(
            date = LocalDateTime.of(2024, 1, 1, 0, 0)
        )

        assertThrows(InvalidDateException::class.java) {
            travelA.addOutlay(outlay)
        }
    }

    @Test
    fun `should throw InvalidDateException when outlay date is after travel initialDate`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 5, 0, 0)
        )
        val outlay = sampleOutlay().copy(
            date = LocalDateTime.of(2024, 1, 6, 0, 0)
        )

        assertThrows(InvalidDateException::class.java) {
            travelA.addOutlay(outlay)
        }
    }

    @Test
    fun `should throw DuplicatedItemsException when the outlay id already exists in list`() {
        val outlay = sampleOutlay()
        travel.addOutlay(outlay)

        assertThrows(DuplicatedItemsException::class.java) {
            travel.addOutlay(outlay)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // addAid()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should add a travelAid when data is correct`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
        val aid = sampleTravelAid().copy(
            date = LocalDateTime.of(2024, 1, 2, 0, 0)
        )

        travelA.addAid(aid)

        val actual = travelA.aids[0]

        assertEquals(aid, actual)
    }

    @Test
    fun `should add a travelAid when travel initialDate and travelAid date are equal`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
        val aid = sampleTravelAid().copy(
            date = LocalDateTime.of(2024, 1, 1, 0, 0)
        )

        travelA.addAid(aid)

        val actual = travelA.aids[0]

        assertEquals(aid, actual)
    }

    @Test
    fun `should add a travelAid when travel finalDate and travelAid date are equal`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 10, 0, 0)
        )
        val aid = sampleTravelAid().copy(
            date = LocalDateTime.of(2024, 1, 10, 0, 0)
        )

        travelA.addAid(aid)

        val actual = travelA.aids[0]

        assertEquals(aid, actual)
    }

    @Test
    fun `should throw InvalidDateException when travelAid date is before travel initialDate`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 2, 0, 0),
        )
        val aid = sampleTravelAid().copy(
            date = LocalDateTime.of(2024, 1, 1, 0, 0)
        )

        assertThrows(InvalidDateException::class.java) {
            travelA.addAid(aid)
        }
    }

    @Test
    fun `should throw InvalidDateException when travelAid loadingDate is after travel initialDate`() {
        val travelA = sampleTravel().copy(
            initialDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = LocalDateTime.of(2024, 1, 5, 0, 0)
        )
        val aid = sampleTravelAid().copy(
            date = LocalDateTime.of(2024, 1, 6, 0, 0)
        )

        assertThrows(InvalidDateException::class.java) {
            travelA.addAid(aid)
        }
    }

    @Test
    fun `should throw DuplicatedItemsException when the travelAid id already exists in list`() {
        val aid = sampleTravelAid()
        travel.addAid(aid)

        assertThrows(DuplicatedItemsException::class.java) {
            travel.addAid(aid)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getProfitPercent()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the profit percent of travels`() {
        travel.run {
            addFreight(sampleFreight())
            addRefuel(sampleRefuel())
        }
        val travelA = sampleTravel().copy(id = "travelId2").apply {
            addFreight(sampleFreight().copy(travelId = "travelId2", value = BigDecimal("12000.00")))
            addRefuel(sampleRefuel().copy(travelId = "travelId2", totalValue = BigDecimal("5500.00")))
        }

        val expected = BigDecimal("47.00")
        val actual = listOf(travel, travelA).getProfitPercent()

        assertEquals(expected, actual)

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