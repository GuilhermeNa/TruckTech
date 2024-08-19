package br.com.apps.model.model.travel

import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.travel.Outlay.Companion.merge
import br.com.apps.model.model.travel.Outlay.Companion.mergePayables
import br.com.apps.model.test_cases.sampleCostLabel
import br.com.apps.model.test_cases.sampleEmployeePayable
import br.com.apps.model.test_cases.sampleOutlay
import br.com.apps.model.test_cases.sampleOutlayDto
import br.com.apps.model.util.ERROR_STRING
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.lang.invoke.WrongMethodTypeException

class OutlayTest {

    private lateinit var outlay: Outlay

    @Before
    fun setup() {
        outlay = sampleOutlay()
    }

    //---------------------------------------------------------------------------------------------//
    // setLabelById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should define the label for this expend`() {
        outlay.setLabelById(listOf(sampleCostLabel()))

        val expected = sampleCostLabel()
        val actual = outlay.label

        assertEquals(expected, actual)
    }

    @Test
    fun `should throw exception when list of labels is empty`() {
        assertThrows(EmptyDataException::class.java) {
            outlay.setLabelById(emptyList())
        }
    }

    @Test
    fun `should throw exception when label is not found`() {
        val label = sampleCostLabel().apply { id = "anotherId1" }
        assertThrows(NullLabelException::class.java) {
            outlay.setLabelById(listOf(label))
        }
    }

    //---------------------------------------------------------------------------------------------//
    // getLabelName()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the label name`() {
        outlay.setLabelById(listOf(sampleCostLabel()))

        val expected = "Name1"
        val name = outlay.getLabelName()

        assertEquals(expected, name)
    }

    @Test
    fun `should return default error text for name when the customer is null`() {
        val name = outlay.getLabelName()

        assertEquals(ERROR_STRING, name)
    }

    //---------------------------------------------------------------------------------------------//
    // merge() Labels
    //---------------------------------------------------------------------------------------------//

    @Test
    fun`should merge a expend list and a label list`() {
        val label = sampleCostLabel()

        listOf(outlay).merge(listOf(label))

        assertEquals(label, outlay.label)
    }

    @Test
    fun`should throw exception for merge when customer list is null`() {
        assertThrows(Exception::class.java) {
            listOf(outlay).merge(emptyList<Label>())
        }
    }

    //---------------------------------------------------------------------------------------------//
    // merge() Payables
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge outlay list to employeePayable`() {
        val outlays = listOf(
            sampleOutlay(),
            sampleOutlay().copy(id = "outlayId2")
        )
        val payables = listOf(
            sampleEmployeePayable().copy(
                parentId = "outlayId1",
                id = "transactionId1",
                type = EmployeePayableTicket.OUTLAY
            ),
            sampleEmployeePayable().copy(
                parentId = "outlayId2",
                id = "transactionId2",
                type = EmployeePayableTicket.OUTLAY
            ),
        )

        outlays.mergePayables(payables)

        val expected = "transactionId1"
        val actual = outlays[0].payable?.id
        assertEquals(expected, actual)

        val expectedB = "transactionId2"
        val actualB = outlays[1].payable?.id
        assertEquals(expectedB, actualB)

    }

    @Test
    fun `should fail on merging with wrong ids`() {
        val outlays = listOf(
            sampleOutlay()
        )
        val payables = listOf(
            sampleEmployeePayable().copy(
                parentId = "wrongId",
                id = "transactionId1",
                type = EmployeePayableTicket.OUTLAY
            )
        )

        outlays.mergePayables(payables)

        val actual = outlays[0].payable?.id
        Assert.assertNull(actual)

    }

    @Test
    fun `should throw WrongMethodTypeException when merging with commission type`() {
        val outlays = listOf(
            sampleOutlay()
        )
        val payables = listOf(
            sampleEmployeePayable().copy(
                parentId = "outlayId1",
                id = "transactionId1",
                type = EmployeePayableTicket.COMMISSION
            )
        )

        assertThrows(WrongMethodTypeException::class.java) {
            outlays.mergePayables(payables)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // setPayables()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should set its payable when the ids match`() {
        val expected = sampleEmployeePayable().copy(parentId = "outlayId1", type = EmployeePayableTicket.OUTLAY)
        outlay.setPayable(expected)

        val actual = outlay.payable

        assertEquals(expected, actual)

    }

    @Test
    fun `should throw InvalidIdException when ids dont match`() {
        val expected = sampleEmployeePayable().copy(parentId = "wrongId")

        assertThrows(InvalidIdException::class.java) {
            outlay.setPayable(expected)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when type is commission`() {
        val expected = sampleEmployeePayable().copy(
            parentId = "outlayId1",
            type = EmployeePayableTicket.COMMISSION
        )

        assertThrows(WrongMethodTypeException::class.java) {
            outlay.setPayable(expected)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // toDto()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return an dto when call toDto`() {
        val expected = sampleOutlayDto()

        val dto = outlay.toDto()

        assertEquals(expected, dto)
    }



}