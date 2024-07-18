package br.com.apps.model.model

import br.com.apps.model.exceptions.InvalidValueException
import br.com.apps.model.model.payroll.Loan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class LoanTest {

    private lateinit var loan: Loan

    @Before
    fun setup() {
        loan = Loan(
            masterUid = "1",
            id = "2",
            employeeId = "3",
            date = LocalDateTime.now(),
            value = BigDecimal("1000.00"),
            installments = 2,
            installmentsAlreadyPaid = 0,
            isPaid = false
        )
    }

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return the installment value when the data is good`() {
        loan.apply {
            value = BigDecimal("1000.00")
            installments = 2
        }
        val value = loan.getInstallmentValue()
        assertEquals(BigDecimal("500.00"), value)
    }

    @Test
    fun `should throw InvalidValueException when installment is not defined or zero`() {
        loan.installments = 0
        assertThrows(InvalidValueException::class.java) { loan.getInstallmentValue() }
    }

}