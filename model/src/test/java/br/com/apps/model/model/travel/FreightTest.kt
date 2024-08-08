package br.com.apps.model.model.travel

import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.null_objects.NullCustomerException
import br.com.apps.model.model.travel.Freight.Companion.merge
import br.com.apps.model.test_cases.sampleCustomer
import br.com.apps.model.test_cases.sampleFreight
import br.com.apps.model.util.ERROR_STRING
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
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
    // merge()
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

}