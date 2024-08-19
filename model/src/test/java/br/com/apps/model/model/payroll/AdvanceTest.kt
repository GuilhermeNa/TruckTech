package br.com.apps.model.model.payroll

import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.model.payroll.Advance.Companion.merge
import br.com.apps.model.test_cases.sampleAdvance
import br.com.apps.model.test_cases.sampleAdvanceDto
import br.com.apps.model.test_cases.sampleEmployeeReceivable
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.lang.invoke.WrongMethodTypeException

class AdvanceTest {

    private lateinit var advance: Advance

    @Before
    fun setup() {
        advance = sampleAdvance()
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge advance list to employeeReceivables`() {
        val advances = listOf(
            sampleAdvance(),
            sampleAdvance().copy(id = "advanceId2")
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "advanceId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.ADVANCE
            ),
            sampleEmployeeReceivable().copy(
                parentId = "advanceId2",
                id = "transactionId2",
                type = EmployeeReceivableTicket.ADVANCE
            )
        )

        advances.merge(receivables)

        val expected = "transactionId1"
        val actual = advances[0].receivable?.id
        Assert.assertEquals(expected, actual)

        val expectedB = "transactionId2"
        val actualB = advances[1].receivable?.id
        Assert.assertEquals(expectedB, actualB)

    }

    @Test
    fun `should fail on merging with wrong ids`() {
        val advances = listOf(
            sampleAdvance()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "advanceId2",
                id = "transactionId1",
                type = EmployeeReceivableTicket.ADVANCE
            )
        )

        advances.merge(receivables)

        val actual = advances[0].receivable?.id
        Assert.assertNull(actual)

    }

    @Test
    fun `should throw WrongMethodTypeException when merging with loan type`() {
        val advances = listOf(
            sampleAdvance()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "advanceId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.LOAN
            )
        )

        assertThrows(WrongMethodTypeException::class.java) {
            advances.merge(receivables)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when merging with aid type`() {
        val advances = listOf(
            sampleAdvance()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "advanceId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.TRAVEL_AID
            )
        )

        assertThrows(WrongMethodTypeException::class.java) {
            advances.merge(receivables)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // setReceivable()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should set its receivable when the ids match`() {
        val expected = sampleEmployeeReceivable().copy(parentId = "advanceId1", type = EmployeeReceivableTicket.ADVANCE)
        advance.setReceivable(expected)

        val actual = advance.receivable

        Assert.assertEquals(expected, actual)

    }

    @Test
    fun `should throw  InvalidIdException when ids dont match`() {
        val expected = sampleEmployeeReceivable().copy(parentId = "advanceId2")

        assertThrows(InvalidIdException::class.java) {
            advance.setReceivable(expected)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when type is loan`() {
        val expected = sampleEmployeeReceivable().copy(
            parentId = "advanceId1",
            type = EmployeeReceivableTicket.LOAN
        )

        assertThrows(WrongMethodTypeException::class.java) {
            advance.setReceivable(expected)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when type is aid`() {
        val expected = sampleEmployeeReceivable().copy(
            parentId = "advanceId1",
            type = EmployeeReceivableTicket.TRAVEL_AID
        )

        assertThrows(WrongMethodTypeException::class.java) {
            advance.setReceivable(expected)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // toDto()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return an dto when call toDto`() {
        val expected = sampleAdvanceDto()

        val dto = advance.toDto()

        Assert.assertEquals(expected, dto)
    }

}