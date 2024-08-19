package br.com.apps.model.model.travel

import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.model.travel.TravelAid.Companion.merge
import br.com.apps.model.test_cases.sampleEmployeeReceivable
import br.com.apps.model.test_cases.sampleTravelAid
import br.com.apps.model.test_cases.sampleTravelAidDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.lang.invoke.WrongMethodTypeException

class TravelAidTest {

    private lateinit var aid: TravelAid

    @Before
    fun setup() {
        aid = sampleTravelAid()
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge aid list to employeeReceivables`() {
        val aids = listOf(
            sampleTravelAid(),
            sampleTravelAid().copy(id = "travelAidId2")
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "travelAidId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.TRAVEL_AID
            ),
            sampleEmployeeReceivable().copy(
                parentId = "travelAidId2",
                id = "transactionId2",
                type = EmployeeReceivableTicket.TRAVEL_AID
            )
        )

        aids.merge(receivables)

        val expected = "transactionId1"
        val actual = aids[0].receivable?.id
        assertEquals(expected, actual)

        val expectedB = "transactionId2"
        val actualB = aids[1].receivable?.id
        assertEquals(expectedB, actualB)

    }

    @Test
    fun `should fail on merging with wrong ids`() {
        val aids = listOf(
            sampleTravelAid()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "travelAidId2",
                id = "transactionId1",
                type = EmployeeReceivableTicket.TRAVEL_AID
            )
        )

        aids.merge(receivables)

        val actual = aids[0].receivable?.id
        assertNull(actual)

    }

    @Test
    fun `should throw WrongMethodTypeException when merging with loan type`() {
        val aids = listOf(
            sampleTravelAid()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "travelAidId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.LOAN
            )
        )

        assertThrows(WrongMethodTypeException::class.java) {
            aids.merge(receivables)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when merging with advance type`() {
        val aids = listOf(
            sampleTravelAid()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "travelAidId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.ADVANCE
            )
        )

        assertThrows(WrongMethodTypeException::class.java) {
            aids.merge(receivables)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // setReceivable()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should set its receivable when the ids match`() {
        val expected = sampleEmployeeReceivable().copy(parentId = "travelAidId1", type = EmployeeReceivableTicket.TRAVEL_AID)
        aid.setReceivable(expected)

        val actual = aid.receivable

        assertEquals(expected, actual)

    }

    @Test
    fun `should throw  InvalidIdException when ids dont match`() {
        val expected = sampleEmployeeReceivable().copy(parentId = "travelAidId2")

        assertThrows(InvalidIdException::class.java) {
            aid.setReceivable(expected)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when type is loan`() {
        val expected = sampleEmployeeReceivable().copy(
            parentId = "travelAidId1",
            type = EmployeeReceivableTicket.LOAN
        )

        assertThrows(WrongMethodTypeException::class.java) {
            aid.setReceivable(expected)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when type is advance`() {
        val expected = sampleEmployeeReceivable().copy(
            parentId = "travelAidId1",
            type = EmployeeReceivableTicket.ADVANCE
        )

        assertThrows(WrongMethodTypeException::class.java) {
            aid.setReceivable(expected)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // toDto()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return an dto when call toDto`() {
        val expected = sampleTravelAidDto()

        val dto = aid.toDto()

        assertEquals(expected, dto)
    }


}