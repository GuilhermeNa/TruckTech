package br.com.apps.model.model.travel

import br.com.apps.model.exceptions.DateOrderException
import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.OdometerOrderException
import br.com.apps.model.model.Customer
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.security.InvalidParameterException
import java.time.LocalDateTime

class TravelTest {

    private lateinit var travel: Travel

    @Before
    fun setup() {
        val freightList = listOf(
            Freight(
                masterUid = "1",
                id = "1",
                truckId = "3",
                employeeId = "4",
                travelId = "5",
                customerId = "6",

                origin = "Origem",
                customer = Customer(
                    masterUid = "1",
                    id = "6",
                    cnpj = "00.000.000/0001-00",
                    name = "Customer A"
                ),
                destiny = "Destino",
                weight = BigDecimal(30000),
                cargo = "Carga",
                breakDown = BigDecimal(100),
                value = BigDecimal(12000),
                loadingDate = LocalDateTime.of(2022, 1, 1, 12, 0),

                dailyValue = BigDecimal(10),
                daily = 3,
                dailyTotalValue = BigDecimal(30),

                isCommissionPaid = true,
                commissionPercentual = BigDecimal(10),
                isValid = true
            ),
            Freight(
                masterUid = "2",
                id = "2",
                truckId = "3",
                employeeId = "4",
                travelId = "6",
                customerId = "7",

                origin = "Origem",
                customer = Customer(
                    masterUid = "1",
                    id = "6",
                    cnpj = "00.000.000/0001-00",
                    name = "Customer A"
                ),
                destiny = "Destino",
                weight = BigDecimal(30000),
                cargo = "Carga",
                breakDown = BigDecimal(100),
                value = BigDecimal(12000),
                loadingDate = LocalDateTime.of(2022, 1, 1, 12, 0),

                dailyValue = BigDecimal(10),
                daily = 3,
                dailyTotalValue = BigDecimal(30),

                isCommissionPaid = true,
                commissionPercentual = BigDecimal(10),
                isValid = true
            )
        )
        val refuelList = listOf(
            Refuel(
                masterUid = "1",
                id = "3",
                truckId = "3",
                travelId = "4",
                driverId = "5",
                date = LocalDateTime.of(2022, 1, 1, 12, 0),
                station = "Station",
                odometerMeasure = BigDecimal(10.0),
                valuePerLiter = BigDecimal(4.5),
                amountLiters = BigDecimal(2.0),
                totalValue = BigDecimal(9.0),
                isCompleteRefuel = true,
                isValid = true
            ),
            Refuel(
                masterUid = "2",
                id = "4",
                truckId = "4",
                travelId = "5",
                driverId = "6",
                date = LocalDateTime.of(2022, 1, 1, 12, 0),
                station = "Station",
                odometerMeasure = BigDecimal(30.0),
                valuePerLiter = BigDecimal(4.5),
                amountLiters = BigDecimal(2.0),
                totalValue = BigDecimal(9.0),
                isCompleteRefuel = true,
                isValid = true
            )
        )
        val expendList = listOf(
            Expend(
                masterUid = "1",
                id = "5",
                truckId = "3",
                driverId = "4",
                travelId = "5",
                labelId = "6",
                company = "Company",
                date = LocalDateTime.of(2022, 1, 1, 12, 0),
                description = "Description",
                value = BigDecimal(100.0),
                label = null,
                isPaidByEmployee = true,
                isAlreadyRefunded = true,
                isValid = true
            ),
            Expend(
                masterUid = "2",
                id = "6",
                truckId = "3",
                driverId = "4",
                travelId = "6",
                labelId = "7",
                company = "Company",
                date = LocalDateTime.of(2022, 1, 4, 12, 0),
                description = "Description",
                value = BigDecimal(100.0),
                label = null,
                isPaidByEmployee = true,
                isAlreadyRefunded = true,
                isValid = true
            )
        )
        val aidList = listOf(
            TravelAid(
                masterUid = "1",
                id = "7",
                driverId = "4",
                travelId = "2",
                date = LocalDateTime.of(2022, 1, 2, 12, 0),
                value = BigDecimal(165.0),
                isPaid = true
            ),
            TravelAid(
                masterUid = "1",
                id = "8",
                driverId = "4",
                travelId = "2",
                date = LocalDateTime.of(2022, 1, 3, 12, 0),
                value = BigDecimal(200.0),
                isPaid = false
            )
        )
        travel = Travel(
            masterUid = "1",
            id = "2",
            truckId = "3",
            driverId = "4",

            isFinished = false,
            considerAverage = true,

            initialDate = LocalDateTime.of(2022, 1, 1, 12, 0),
            finalDate = LocalDateTime.of(2022, 1, 5, 12, 0),
            initialOdometerMeasurement = BigDecimal(10.0),
            finalOdometerMeasurement = BigDecimal(30.0),
            freightsList = freightList,
            refuelsList = refuelList,
            expendsList = expendList,
            aidList = aidList
        )
    }

    //---------------------------------------------------------------------------------------------//
    // getListSize()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the size of the list for freights`() {
        val freightSize = travel.getListSize(Travel.FREIGHT)
        assertEquals(2, freightSize)
    }

    @Test
    fun `should return the size of the list for expends`() {
        val expendSize = travel.getListSize(Travel.EXPEND)
        assertEquals(2, expendSize)
    }

    @Test
    fun `should return the size of the list for refuels`() {
        val refuelSize = travel.getListSize(Travel.REFUEL)
        assertEquals(2, refuelSize)
    }

    @Test
    fun `should return the size of the list for aids`() {
        val aidSize = travel.getListSize(Travel.AID)
        assertEquals(2, aidSize)
    }

    @Test
    fun `should return size zero when the freight list is null`() {
        travel.freightsList = null
        val freightSize = travel.getListSize(Travel.FREIGHT)
        assertEquals(0, freightSize)
    }

    @Test
    fun `should return size zero when the refuel list is null`() {
        travel.refuelsList = null
        val refuelSize = travel.getListSize(Travel.REFUEL)
        assertEquals(0, refuelSize)
    }

    @Test
    fun `should return size zero when the expend list is null`() {
        travel.expendsList = null
        val expendSize = travel.getListSize(Travel.EXPEND)
        assertEquals(0, expendSize)
    }

    @Test
    fun `should return size zero when the aid list is null`() {
        travel.aidList = null
        val aidSize = travel.getListSize(Travel.AID)
        assertEquals(0, aidSize)

    }

    @Test
    fun `should throw exception when the int tag is wrong for list size`() {
        Assert.assertThrows(InvalidParameterException::class.java) {
            travel.getListSize(999)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getListOfIdsForList()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the freight list of ids for a selected item`() {
        val freightIds = travel.getListOfIdsForList(Travel.FREIGHT)
        assertEquals(listOf("1", "2"), freightIds)
    }

    @Test
    fun `should return the refuel list of ids for a selected item`() {
        val refuelIds = travel.getListOfIdsForList(Travel.REFUEL)
        assertEquals(listOf("3", "4"), refuelIds)
    }

    @Test
    fun `should return the expend list of ids for a selected item`() {
        val expendIds = travel.getListOfIdsForList(Travel.EXPEND)
        assertEquals(listOf("5", "6"), expendIds)
    }

    @Test
    fun `should return the aid list of ids for a selected item`() {
        val aidIds = travel.getListOfIdsForList(Travel.AID)
        assertEquals(listOf("7", "8"), aidIds)
    }

    @Test
    fun `should return an empty list when the freight list is null`() {
        travel.freightsList = null
        val freightList = travel.getListOfIdsForList(Travel.FREIGHT)
        assertEquals(emptyList<String>(), freightList)
    }

    @Test
    fun `should return an empty list when the refuel list is null`() {
        travel.refuelsList = null
        val refuelList = travel.getListOfIdsForList(Travel.REFUEL)
        assertEquals(emptyList<String>(), refuelList)
    }

    @Test
    fun `should return an empty list when the expend list is null`() {
        travel.expendsList = null
        val expendList = travel.getListOfIdsForList(Travel.EXPEND)
        assertEquals(emptyList<String>(), expendList)
    }

    @Test
    fun `should return an empty list when the aid list is null`() {
       travel.aidList = null
        val aidList = travel.getListOfIdsForList(Travel.AID)
        assertEquals(emptyList<String>(), aidList)
    }

    @Test
    fun `should throw exception when the int tag is wrong for list of ids`() {
        Assert.assertThrows(InvalidParameterException::class.java) {
            travel.getListOfIdsForList(999)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getCommissionValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the commission value when the freight list is good`() {
        travel.freightsList!![0].apply {
            value = BigDecimal(100.0)
            commissionPercentual = BigDecimal(10.0)
        }
        travel.freightsList!![1].apply {
            value = BigDecimal(200)
            commissionPercentual = BigDecimal(10.0)
        }
        val commissionValue = travel.getCommissionValue()
        assertEquals(BigDecimal(30.0).setScale(2), commissionValue)
    }

    @Test
    fun `should return zero when the freight list is null`() {
        travel.freightsList = null
        val commissionValue = travel.getCommissionValue()
        assertEquals(BigDecimal.ZERO, commissionValue)
    }

    //---------------------------------------------------------------------------------------------//
    // getDifferenceBetweenInitialAndFinalOdometerMeasure()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the difference between initial and final odometer measure`() {
        val difference = travel.getDifferenceBetweenInitialAndFinalOdometerMeasure()
        assertEquals(BigDecimal(20.0), difference)
    }

    @Test
    fun `should return zero when any odometer measure is null`() {
        travel.finalOdometerMeasurement = null
        val difference = travel.getDifferenceBetweenInitialAndFinalOdometerMeasure()
        assertEquals(BigDecimal.ZERO, difference)
    }

    //---------------------------------------------------------------------------------------------//
    // getListTotalValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the sum of values for the list tag - Freights`() {
        travel.freightsList!!.run {
            this[0].value = BigDecimal(10.0)
            this[1].value = BigDecimal(30.0)
        }
        val freightSum = travel.getListTotalValue(Travel.FREIGHT)
        assertEquals(BigDecimal(40.0), freightSum)
    }

    @Test
    fun `should return the sum of values for the list tag - Refuels`() {
        travel.refuelsList!!.run {
            this[0].totalValue = BigDecimal(20.0)
            this[1].totalValue = BigDecimal(5.0)
        }
        val refuelSum = travel.getListTotalValue(Travel.REFUEL)
        assertEquals(BigDecimal(25.0), refuelSum)
    }

    @Test
    fun `should return the sum of values for the list tag - Expends`() {
        travel.expendsList!!.run {
            this[0].value = BigDecimal(15.0)
            this[1].value = BigDecimal(20.0)
        }
        val expendSum = travel.getListTotalValue(Travel.EXPEND)
        assertEquals(BigDecimal(35.0), expendSum)
    }

    @Test
    fun `should return the sum of values for the aid list tag - Aid`() {
        travel.aidList!!.run {
            this[0].value = BigDecimal(10.0)
            this[1].value = BigDecimal(8.0)
        }
        val aidSum = travel.getListTotalValue(Travel.AID)
        assertEquals(BigDecimal(18.0), aidSum)
    }

    @Test
    fun `should return zero when the list is null for sum of values - Freights`() {
        travel.freightsList = null
        val freightSum = travel.getListTotalValue(Travel.FREIGHT)
        assertEquals(BigDecimal.ZERO, freightSum)
    }

    @Test
    fun `should return zero when the list is null for sum of values - Refuels`() {
        travel.refuelsList = null
        val refuelSum = travel.getListTotalValue(Travel.REFUEL)
        assertEquals(BigDecimal.ZERO, refuelSum)
    }

    @Test
    fun `should return zero when the list is null for sum of values - Expends`() {
        travel.expendsList = null
        val expendSum = travel.getListTotalValue(Travel.EXPEND)
        assertEquals(BigDecimal.ZERO, expendSum)
    }

    @Test
    fun `should return zero when the list is null for sum of values - Aid`() {
        travel.aidList = null
        val aidSum = travel.getListTotalValue(Travel.AID)
        assertEquals(BigDecimal.ZERO, aidSum)
    }

    @Test
    fun `should throw exception when the int tag is wrong for sum of values`() {
        Assert.assertThrows(InvalidParameterException::class.java) {
            travel.getListTotalValue(999)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getLiquidValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the liquid value for this travel when the values are good`() {
        travel.apply {
            freightsList!!.run {
                this[0].value = BigDecimal(10.0)
                this[1].value = BigDecimal(10.0)
            }
            refuelsList!!.run {
                this[0].totalValue = BigDecimal(3.0)
                this[1].totalValue = BigDecimal(3.0)
            }
            expendsList!!.run {
                this[0].value = BigDecimal(1.5)
                this[1].value = BigDecimal(1.5)
            }
        }
        val liquidValue = travel.getLiquidValue()
        assertEquals(BigDecimal(9.00).setScale(2), liquidValue)
    }

    @Test
    fun `should return the liquid value when the value is negative`() {
        travel.apply {
            freightsList!!.run {
                this[0].value = BigDecimal("4.00")
                this[1].value = BigDecimal("4.00")
            }
            refuelsList!!.run {
                this[0].totalValue = BigDecimal("3.00")
                this[1].totalValue = BigDecimal("3.00")
            }
            expendsList!!.run {
                this[0].value = BigDecimal("2.00")
                this[1].value = BigDecimal("2.00")
            }
        }
        val liquidValue = travel.getLiquidValue()
        assertEquals(BigDecimal("-2.80"), liquidValue)
    }

    @Test
    fun `should return the liquid value with null freight list`() {
        travel.apply {
            freightsList = null
            refuelsList!!.run {
                this[0].totalValue = BigDecimal("3.00")
                this[1].totalValue = BigDecimal("3.00")
            }
            expendsList!!.run {
                this[0].value = BigDecimal("1.50")
                this[1].value = BigDecimal("1.50")
            }
        }
        val liquidValueWhenNull = travel.getLiquidValue()
        assertEquals(BigDecimal("-9.00"), liquidValueWhenNull)
    }

    @Test
    fun `should return the liquid value with empty freight list`() {
        travel.apply {
            freightsList = emptyList()
            refuelsList!!.run {
                this[0].totalValue = BigDecimal("3.00")
                this[1].totalValue = BigDecimal("3.00")
            }
            expendsList!!.run {
                this[0].value = BigDecimal("1.50")
                this[1].value = BigDecimal("1.50")
            }
        }
        val liquidValueWhenEmpty = travel.getLiquidValue()
        assertEquals(BigDecimal("-9.00"), liquidValueWhenEmpty)
    }

    @Test
    fun `should return the liquid value with null refuel list`() {
        travel.apply {
            freightsList!!.run {
                this[0].value = BigDecimal(10.0)
                this[1].value = BigDecimal(10.0)
            }
            refuelsList = null
            expendsList!!.run {
                this[0].value = BigDecimal(3.0)
                this[1].value = BigDecimal(3.0)
            }
        }
        val liquidValueWhenNull = travel.getLiquidValue()
        assertEquals(BigDecimal("12.00"), liquidValueWhenNull)
    }

    @Test
    fun `should return the liquid value with empty refuel list`() {
        travel.apply {
            freightsList!!.run {
                this[0].value = BigDecimal(10.0)
                this[1].value = BigDecimal(10.0)
            }
            refuelsList = emptyList()
            expendsList!!.run {
                this[0].value = BigDecimal(3.0)
                this[1].value = BigDecimal(3.0)
            }
        }
        val liquidValueWhenEmpty = travel.getLiquidValue()
        assertEquals(BigDecimal("12.00"), liquidValueWhenEmpty)
    }

    @Test
    fun `should return the liquid value with null expend list`() {
        travel.apply {
            freightsList!!.run {
                this[0].value = BigDecimal(10.0)
                this[1].value = BigDecimal(10.0)
            }
            refuelsList!!.run {
                this[0].totalValue = BigDecimal(3.0)
                this[1].totalValue = BigDecimal(3.0)
            }
            expendsList = null
        }
        val liquidValueWhenNull = travel.getLiquidValue()
        assertEquals(BigDecimal("12.00"), liquidValueWhenNull)
    }

    @Test
    fun `should return the liquid value with empty expend list`() {
        travel.apply {
            freightsList!!.run {
                this[0].value = BigDecimal(10.0)
                this[1].value = BigDecimal(10.0)
            }
            refuelsList!!.run {
                this[0].totalValue = BigDecimal(3.0)
                this[1].totalValue = BigDecimal(3.0)
            }
            expendsList = emptyList()
        }
        val liquidValueWhenEmpty = travel.getLiquidValue()
        assertEquals(BigDecimal("12.00"), liquidValueWhenEmpty)
    }

    //---------------------------------------------------------------------------------------------//
    // getTravelAuthenticationPercent()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the auth percent when values are good`() {
        // Test 01
        travel.apply {
            freightsList!!.run {
                this[0].isValid = false
                this[1].isValid = false
            }
            refuelsList!!.run {
                this[0].isValid = false
                this[1].isValid = false
            }
            expendsList!!.run {
                this[0].isValid = false
                this[1].isValid = false
            }
        }
        val authPercZero = travel.getTravelAuthenticationPercent()
        assertEquals(0.0, authPercZero, 0.0)

        // Test 02
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
        }
        val authPercHalf = travel.getTravelAuthenticationPercent()
        assertEquals(50.0, authPercHalf, 0.0)

        // Test 03
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
        }
        val authPercTotal = travel.getTravelAuthenticationPercent()
        assertEquals(100.0, authPercTotal, 0.0)
    }

    @Test
    fun `should return the auth percent when freight list is null or empty`() {
        // Test 01
        travel.apply {
            freightsList = null
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
        }
        val authPercWhenNull = travel.getTravelAuthenticationPercent()
        assertEquals(50.0, authPercWhenNull, 0.0)

        // Test 02
        travel.apply {
            freightsList = emptyList()
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
        }
        val authPercWhenEmpty = travel.getTravelAuthenticationPercent()
        assertEquals(50.0, authPercWhenEmpty, 0.0)
    }

    @Test
    fun `should return the auth percent when refuel list is null or empty`() {
        // Test 01
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            refuelsList = null
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
        }
        val authPercWheNull = travel.getTravelAuthenticationPercent()
        assertEquals(50.0, authPercWheNull, 0.0)

        // Test 02
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            refuelsList = emptyList()
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
        }
        val authPercWhenEmpty = travel.getTravelAuthenticationPercent()
        assertEquals(50.0, authPercWhenEmpty, 0.0)
    }

    @Test
    fun `should return the auth percent when expend list is null or empty`() {
        // Test 01
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            expendsList = null
        }
        val authPercNull = travel.getTravelAuthenticationPercent()
        assertEquals(50.0, authPercNull, 0.0)

        // Test02
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = false
            }
            expendsList = emptyList()
        }
        val authPercEmpty = travel.getTravelAuthenticationPercent()
        assertEquals(50.0, authPercEmpty, 0.0)
    }

    //---------------------------------------------------------------------------------------------//
    // isReadyToBeFinished()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true when the travel fully valid`() {
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
        }
        Assert.assertTrue(travel.isReadyToBeFinished())
    }

    @Test
    fun `should return false when the travel is not fully valid`() {
        // Test 01
        travel.apply {
            freightsList!!.run {
                this[0].isValid = false
                this[1].isValid = true
            }
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
        }
        Assert.assertFalse(travel.isReadyToBeFinished())

        // Test 02
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
            refuelsList!!.run {
                this[0].isValid = false
                this[1].isValid = true
            }
            expendsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
        }
        Assert.assertFalse(travel.isReadyToBeFinished())

        // Test03
        travel.apply {
            freightsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
            refuelsList!!.run {
                this[0].isValid = true
                this[1].isValid = true
            }
            expendsList!!.run {
                this[0].isValid = false
                this[1].isValid = true
            }
        }
        Assert.assertFalse(travel.isReadyToBeFinished())
    }

    //---------------------------------------------------------------------------------------------//
    // isEmptyTravel()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return false when the travel has registered itens`() {
        Assert.assertFalse(travel.isEmptyTravel())
    }

    @Test
    fun `should return true when the travel has no registered itens or are null`() {
        // Test 01
        travel.apply {
            freightsList = emptyList()
            refuelsList = emptyList()
            expendsList = emptyList()
            aidList = emptyList()
        }
        Assert.assertTrue(travel.isEmptyTravel())

        // Test 02
        travel.apply {
            freightsList = null
            refuelsList = null
            expendsList = null
            aidList = null
        }
        Assert.assertTrue(travel.isEmptyTravel())
    }

    //---------------------------------------------------------------------------------------------//
    // validateForSaving()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw odometerOrderException when odometer measures are in wrong order`() {
        travel.apply {
            initialOdometerMeasurement = BigDecimal(5.0)
            finalOdometerMeasurement = BigDecimal(1.0)
        }
        Assert.assertThrows(OdometerOrderException::class.java) { travel.validateForSaving() }
    }

    @Test
    fun `should throw DuplicatedItemsException when there is duplicated items on lists`() {
        // Test 01
        travel.freightsList!!.run {
            this[0].id = "1"
            this[1].id = "1"
        }
        Assert.assertThrows(DuplicatedItemsException::class.java) { travel.validateForSaving() }

        // Test 02
        travel.refuelsList!!.run {
            this[0].id = "1"
            this[1].id = "1"
        }
        Assert.assertThrows(DuplicatedItemsException::class.java) { travel.validateForSaving() }

        // Test 03
        travel.expendsList!!.run {
            this[0].id = "1"
            this[1].id = "1"
        }
        Assert.assertThrows(DuplicatedItemsException::class.java) { travel.validateForSaving() }

        // Test 04
        travel.aidList!!.run {
            this[0].id = "1"
            this[1].id = "1"
        }
        Assert.assertThrows(DuplicatedItemsException::class.java) { travel.validateForSaving() }
    }

    @Test
    fun `should throw dateOrderException when dates are in wrong order`() {
        travel.apply {
            initialDate = LocalDateTime.of(2022, 1, 2, 12, 0)
            finalDate = LocalDateTime.of(2022, 1, 1, 12, 0)
        }
        Assert.assertThrows(DateOrderException::class.java) { travel.validateForSaving() }
    }

    @Test
    fun `should throw dateOrderException when final date is null`() {
        travel.apply {
            initialDate = LocalDateTime.of(2022, 1, 2, 12, 0)
            finalDate = null
        }
        Assert.assertThrows(NullPointerException::class.java) { travel.validateForSaving() }
    }

    @Test
    fun `should throw exception when there is any problem with freights`() {
        // Test 01
        travel.freightsList!!.run { this[0].isValid = false }
        Assert.assertThrows(InvalidParameterException::class.java) {
            travel.validateForSaving()
        }

        // Test 02
        travel.freightsList = emptyList()
        Assert.assertThrows(EmptyDataException::class.java) {
            travel.validateForSaving()
        }

        // Test 03
        travel.freightsList = null
        Assert.assertThrows(NullPointerException::class.java) {
            travel.validateForSaving()
        }
    }

    @Test
    fun `should throw exception when there is any problem with refuels`() {
        travel.refuelsList!!.run { this[0].isValid = false }
        Assert.assertThrows(InvalidParameterException::class.java) {
            travel.validateForSaving()
        }
    }

    @Test
    fun `should throw exception when there is any problem with expends`() {
        travel.expendsList!!.run { this[0].isValid = false }
        Assert.assertThrows(InvalidParameterException::class.java) {
            travel.validateForSaving()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // isDeletable()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true when travel can be deleted`() {
        travel.apply {
            isFinished = false
            freightsList!!.run {
                this[0].isValid = false
                this[1].isValid = false
            }
            refuelsList!!.run {
                this[0].isValid = false
                this[1].isValid = false
            }
            expendsList!!.run {
                this[0].isValid = false
                this[1].isValid = false
            }
            aidList = emptyList()
        }
        Assert.assertTrue(travel.isDeletable())
    }

    @Test
    fun `should return false when travel cannot be deleted because is finished or id is null`() {
        // Test 01
        travel.apply {
            isFinished = true
            freightsList = emptyList()
            refuelsList = emptyList()
            expendsList = emptyList()
            aidList = emptyList()
        }
        Assert.assertFalse(travel.isDeletable())

        // Test 02
        travel.apply {
            isFinished = false
            id = null
        }
        Assert.assertFalse(travel.isDeletable())
    }

    @Test
    fun `should return false when freights cannot be deleted`() {
        // Test 01
        travel.apply {
            isFinished = false
            id = "1"
            freightsList!![0].isValid = true
            refuelsList = emptyList()
            expendsList = emptyList()
            aidList = emptyList()
        }
        Assert.assertFalse(travel.isDeletable())

        // Test 02
        travel.apply {
            isFinished = false
            id = "1"
            freightsList!!.run {
                this[0].isValid = false
                this[0].id = null
            }
            refuelsList = emptyList()
            expendsList = emptyList()
            aidList = emptyList()
        }
        Assert.assertFalse(travel.isDeletable())

    }

    @Test
    fun `should return false when refuels cannot be deleted`() {
        // Test 01
        travel.apply {
            isFinished = false
            id = "1"
            freightsList = emptyList()
            refuelsList!![0].isValid = true
            expendsList = emptyList()
            aidList = emptyList()
        }
        Assert.assertFalse(travel.isDeletable())

        // Test 02
        travel.apply {
            isFinished = false
            id = "1"
            freightsList = emptyList()
            refuelsList!!.run {
                this[0].isValid = false
                this[0].id = null
            }
            expendsList = emptyList()
            aidList = emptyList()
        }
        Assert.assertFalse(travel.isDeletable())
    }

    @Test
    fun `should return false when expends cannot be deleted`() {
        // Test 01
        travel.apply {
            isFinished = false
            id = "1"
            freightsList = emptyList()
            refuelsList = emptyList()
            expendsList!![0].isValid = true
        }
        Assert.assertFalse(travel.isDeletable())

        // Test 02
        travel.apply {
            isFinished = false
            id = "1"
            freightsList = emptyList()
            refuelsList = emptyList()
            expendsList!!.run {
                this[0].isValid = false
                this[0].id = null
            }
        }
        Assert.assertFalse(travel.isDeletable())

    }

    @Test
    fun `should return false when aids cannot be deleted`() {
        travel.apply {
            freightsList = emptyList()
            refuelsList = emptyList()
            expendsList = emptyList()
        }
        Assert.assertFalse(travel.isDeletable())
    }

    //---------------------------------------------------------------------------------------------//
    // getProfitPercent()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the profit percent when the values are good`() {
        travel.apply {
            freightsList!!.run {
                this[0].run {
                    value = BigDecimal(10.00)
                    commissionPercentual = BigDecimal(10.00)
                }
                this[1].run {
                    value = BigDecimal(10.00)
                    commissionPercentual = BigDecimal(10.00)
                }
            }
            refuelsList!!.run {
                this[0].totalValue = BigDecimal(5.00)
                this[1].totalValue = BigDecimal(5.00)
            }
            expendsList!!.run {
                this[0].value = BigDecimal(2.00)
                this[1].value = BigDecimal(2.00)
            }
        }
        val profitPerc = travel.getProfitPercent()
        assertEquals(BigDecimal(20.00).setScale(2), profitPerc)
    }

    @Test
    fun `should return zero profit percent when the freight list is null`() {
        travel.apply {
            freightsList = null
            refuelsList!!.run {
                this[0].totalValue = BigDecimal(5.0)
                this[1].totalValue = BigDecimal(5.0)
            }
            expendsList!!.run {
                this[0].value = BigDecimal(2.0)
                this[0].value = BigDecimal(2.0)
            }
        }

        val profitPerc = travel.getProfitPercent()
        assertEquals(BigDecimal.ZERO, profitPerc)
    }

    @Test
    fun `should return full profit percent when the waste value is null`() {
        travel.apply {
            freightsList!!.run {
                this[0].run {
                    value = BigDecimal(50.0)
                    commissionPercentual = BigDecimal(10.0)
                }
                this[1].run {
                    value = BigDecimal(50.0)
                    commissionPercentual = BigDecimal(10.0)
                }
            }
            refuelsList = null
            expendsList = null
        }

        val profitPerc = travel.getProfitPercent()
        assertEquals(BigDecimal("90.00"), profitPerc)
    }

    //---------------------------------------------------------------------------------------------//
    // getFuelAverage()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the fuel average when values are good`() {
        travel.apply {
            initialOdometerMeasurement = BigDecimal(5.0)
            finalOdometerMeasurement = BigDecimal(30.0)
            refuelsList!!.run {
                this[0].amountLiters = BigDecimal(5.0)
                this[1].amountLiters = BigDecimal(5.0)
            }
        }
        assertEquals(BigDecimal("2.50"), travel.getFuelAverage())
    }

    //---------------------------------------------------------------------------------------------//
    // shouldConsiderAverage()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return true if the last refuel had a complete refuel`() {
        travel.refuelsList!!.run {
            this[1].isCompleteRefuel = true
        }
        Assert.assertTrue(travel.shouldConsiderAverage())
    }

    @Test
    fun `should return false if the last refuel is not complete refuel`() {
        travel.refuelsList!!.run {
            this[1].isCompleteRefuel = false
        }
        Assert.assertFalse(travel.shouldConsiderAverage())
    }

    @Test
    fun `should return false if the refuel list is null or empty`() {
        travel.refuelsList = null
        Assert.assertFalse(travel.shouldConsiderAverage())

        travel.refuelsList = emptyList()
        Assert.assertFalse(travel.shouldConsiderAverage())
    }

}