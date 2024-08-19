package br.com.apps.model.model.travel

import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.exceptions.null_objects.NullCustomerException
import br.com.apps.model.model.travel.Freight.Companion.merge
import br.com.apps.model.model.travel.Freight.Companion.mergePayables
import br.com.apps.model.test_cases.sampleCustomer
import br.com.apps.model.test_cases.sampleEmployeePayable
import br.com.apps.model.test_cases.sampleFreight
import br.com.apps.model.util.ERROR_STRING
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.lang.invoke.WrongMethodTypeException
import java.math.BigDecimal

class FreightTest {

    private lateinit var freight: Freight

    @Before
    fun setup() {
        freight = sampleFreight()
    }

    //---------------------------------------------------------------------------------------------//
    // getCommissionValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the commission value`() {
        val expected = BigDecimal("1000.00")
        val value = freight.getCommissionValue()
        assertEquals(expected, value)
    }

    //---------------------------------------------------------------------------------------------//
    // setCustomerById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should define the customer for this freight`() {
        val expected = sampleCustomer()

        freight.setCustomer(sampleCustomer())

        assertEquals(expected, freight.customer)
    }

    @Test
    fun `should throw exception when list of customers is empty`() {
        assertThrows(EmptyDataException::class.java) {
            freight.setCustomerById(emptyList())
        }
    }

    @Test
    fun `should throw exception when customers is not found`() {
        val customer = sampleCustomer().apply { id = "anotherId1" }

        assertThrows(NullCustomerException::class.java) {
            freight.setCustomerById(listOf(customer))
        }

    }

    //---------------------------------------------------------------------------------------------//
    // getCustomerName()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the customers name`() {
        freight.setCustomerById(listOf(sampleCustomer()))

        val expected = "Name1"
        val actual = freight.getCustomerName()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return default error text for name when the customer is null`() {
        val name = freight.getCustomerName()

        assertEquals(ERROR_STRING, name)
    }

    //---------------------------------------------------------------------------------------------//
    // getCustomerCnpj()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the customers cnpj`() {
        freight.setCustomerById(listOf(sampleCustomer()))

        val expected = "xxx.xxx.xxx/xxxx-xx"
        val actual = freight.getCustomerCnpj()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return default error text for cnpj when the customer is null`() {
        val cnpj = freight.getCustomerCnpj()

        assertEquals(ERROR_STRING, cnpj)
    }

    //---------------------------------------------------------------------------------------------//
    // merge() Labels
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge a freight list and a customer list`() {
        val expected = sampleCustomer()

        listOf(freight).merge(listOf(expected))

        assertEquals(expected, freight.customer)
    }

    @Test
    fun `should throw exception for merge when customer list is null`() {
        assertThrows(Exception::class.java) {
            listOf(freight).merge(emptyList())
        }
    }

    //---------------------------------------------------------------------------------------------//
    // mergePayables()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge freight list to employeeReceivables`() {
        val freights = listOf(
            sampleFreight(),
            sampleFreight().copy(id = "freightId2")
        )
        val receivables = listOf(
            sampleEmployeePayable().copy(
                parentId = "freightId1",
                id = "transactionId1",
                type = EmployeePayableTicket.COMMISSION
            ),
            sampleEmployeePayable().copy(
                parentId = "freightId2",
                id = "transactionId2",
                type = EmployeePayableTicket.COMMISSION
            )
        )

        freights.mergePayables(receivables)

        val expected = "transactionId1"
        val actual = freights[0].payable?.id
        assertEquals(expected, actual)

        val expectedB = "transactionId2"
        val actualB = freights[1].payable?.id
        assertEquals(expectedB, actualB)

    }

    @Test
    fun `should fail on merging with wrong ids`() {
        val freights = listOf(
            sampleFreight()
        )
        val receivables = listOf(
            sampleEmployeePayable().copy(
                parentId = "wrongId",
                id = "transactionId1",
                type = EmployeePayableTicket.COMMISSION
            )
        )

        freights.mergePayables(receivables)

        val actual = freights[0].payable?.id
        Assert.assertNull(actual)

    }

    @Test
    fun `should throw WrongMethodTypeException when merging with outlay type`() {
        val freights = listOf(
            sampleFreight()
        )
        val payables = listOf(
            sampleEmployeePayable().copy(
                parentId = "freightId1",
                id = "transactionId1",
                type = EmployeePayableTicket.OUTLAY
            )
        )

        assertThrows(WrongMethodTypeException::class.java) {
            freights.mergePayables(payables)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // setPayable()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should throw WrongMethodTypeException when the type is salary`() {
        val payable = sampleEmployeePayable().copy(parentId = "freightId1", type = EmployeePayableTicket.SALARY)

        assertThrows(WrongMethodTypeException::class.java) {
            freight.setPayable(payable)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when the type is vacation`() {
        val payable = sampleEmployeePayable().copy(parentId = "freightId1", type = EmployeePayableTicket.VACATION)

        assertThrows(WrongMethodTypeException::class.java) {
            freight.setPayable(payable)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when the type is thirteenth`() {
        val payable = sampleEmployeePayable().copy(parentId = "freightId1", type = EmployeePayableTicket.THIRTEENTH)

        assertThrows(WrongMethodTypeException::class.java) {
            freight.setPayable(payable)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when the type is outlay`() {
        val payable = sampleEmployeePayable().copy(parentId = "freightId1", type = EmployeePayableTicket.OUTLAY)

        assertThrows(WrongMethodTypeException::class.java) {
            freight.setPayable(payable)
        }

    }

    @Test
    fun `should throw InvalidIdException when the payable parent id is wrong`() {
        val payable = sampleEmployeePayable().copy(parentId = "freightId2")

        assertThrows(InvalidIdException::class.java) {
            freight.setPayable(payable)
        }

    }

}