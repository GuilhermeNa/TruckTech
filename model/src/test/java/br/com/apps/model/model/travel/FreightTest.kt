package br.com.apps.model.model.travel

import br.com.apps.model.util.ERROR_STRING
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.null_objects.NullCustomerException
import br.com.apps.model.model.travel.Freight.Companion.merge
import br.com.apps.model.test_cases.sampleCustomer
import br.com.apps.model.test_cases.sampleFreight
import org.junit.Assert
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
        //Test 01
        freight.value = BigDecimal("1000.00")
        val cValueA = freight.getCommissionValue()
        Assert.assertEquals(BigDecimal("100.00"), cValueA)

        //Test 02
        freight.value = BigDecimal("1000.30")
        val cValueB = freight.getCommissionValue()
        Assert.assertEquals(BigDecimal("100.03"), cValueB)

        //Test 03
        freight.value = BigDecimal("1000.49")
        val cValueC = freight.getCommissionValue()
        Assert.assertEquals(BigDecimal("100.05"), cValueC)
    }

    //---------------------------------------------------------------------------------------------//
    // setCustomerById()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should define the customer for this freight`() {
        val customer = sampleCustomer()
        assertEquals(customer, freight.customer)
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
        val name = freight.getCustomerName()
        assertEquals("Name1", name)
    }

    @Test
    fun `should return default error text for name when the customer is null`() {
        freight.customer = null

        val name = freight.getCustomerName()

        assertEquals(ERROR_STRING, name)
    }

    //---------------------------------------------------------------------------------------------//
    // getCustomerCnpj()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the customers cnpj`() {
        val cnpj = freight.getCustomerCnpj()
        assertEquals("xxx.xxx.xxx/xxxx-xx", cnpj)
    }

    @Test
    fun `should return default error text for cnpj when the customer is null`() {
        freight.customer = null

        val cnpj = freight.getCustomerCnpj()

        assertEquals(ERROR_STRING, cnpj)
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun`should merge a freight list and a customer list`() {
        val customer = sampleCustomer()
        freight.customer = null

        listOf(freight).merge(listOf(customer))

        assertEquals(customer, freight.customer)
    }

    @Test
    fun`should throw exception for merge when customer list is null`() {
        freight.customer = null
        assertThrows(Exception::class.java) {
            listOf(freight).merge(emptyList())
        }
    }

}