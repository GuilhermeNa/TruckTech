package br.com.apps.model.model.travel

import br.com.apps.model.model.Customer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class FreightTest {

    private lateinit var freight: Freight

    @Before
    fun setup() {
        freight = Freight(
            masterUid = "1",
            id = "2",
            truckId = "3",
            employeeId = "4",
            travelId = "5",
            customerId = "6",

            origin = "Origem",
            customer = Customer(masterUid = "1", id = "6", cnpj = "00.000.000/0001-00", name = "Customer A"),
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
    }

    @Test
    fun `getCommissionValue() - should return the commission value`() {
        //Test 01
        freight.value = BigDecimal("12000.00")
        val cValueA = freight.getCommissionValue()
        Assert.assertEquals(BigDecimal("1200.00"), cValueA)

        //Test 02
        freight.value = BigDecimal("12000.30")
        val cValueB = freight.getCommissionValue()
        Assert.assertEquals(BigDecimal("1200.03"), cValueB)

        //Test 03
        freight.value = BigDecimal("12000.49")
        val cValueC = freight.getCommissionValue()
        Assert.assertEquals(BigDecimal("1200.05"), cValueC)
    }

}