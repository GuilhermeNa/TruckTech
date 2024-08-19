package br.com.apps.model.model.payroll

import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.invalid.InvalidIdException
import br.com.apps.model.model.payroll.Loan.Companion.merge
import br.com.apps.model.test_cases.sampleEmployeeReceivable
import br.com.apps.model.test_cases.sampleLoan
import br.com.apps.model.test_cases.sampleLoanDto
import br.com.apps.model.test_cases.sampleTransaction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.lang.invoke.WrongMethodTypeException

class LoanTest {

    private lateinit var loan: Loan

    @Before
    fun setup() {
        loan = sampleLoan()
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should merge loan list to employeeReceivables`() {
        val loans = listOf(
            sampleLoan(),
            sampleLoan().copy(id = "loanId2")
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "loanId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.LOAN
            ),
            sampleEmployeeReceivable().copy(
                parentId = "loanId2",
                id = "transactionId2",
                type = EmployeeReceivableTicket.LOAN
            ),
        )

        loans.merge(receivables)

        val expected = "transactionId1"
        val actual = loans[0].receivable?.id
        assertEquals(expected, actual)

        val expectedB = "transactionId2"
        val actualB = loans[1].receivable?.id
        assertEquals(expectedB, actualB)

    }

    @Test
    fun `should fail on merging with wrong ids`() {
        val loans = listOf(
            sampleLoan()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "loanId2",
                id = "transactionId1",
                type = EmployeeReceivableTicket.LOAN
            )
        )

        loans.merge(receivables)

        val actual = loans[0].receivable?.id
        assertNull(actual)

    }

    @Test
    fun `should throw WrongMethodTypeException when merging with advance type`() {
        val loans = listOf(
            sampleLoan()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "loanId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.ADVANCE
            )
        )

        assertThrows(WrongMethodTypeException::class.java) {
            loans.merge(receivables)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when merging with aid type`() {
        val loans = listOf(
            sampleLoan()
        )
        val receivables = listOf(
            sampleEmployeeReceivable().copy(
                parentId = "loanId1",
                id = "transactionId1",
                type = EmployeeReceivableTicket.TRAVEL_AID
            )
        )

        assertThrows(WrongMethodTypeException::class.java) {
            loans.merge(receivables)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // setReceivable()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should set its receivable when the ids match`() {
        val expected = sampleEmployeeReceivable().copy(
            parentId = "loanId1",
            type = EmployeeReceivableTicket.LOAN
        )
        loan.setReceivable(expected)

        val actual = loan.receivable

        assertEquals(expected, actual)

    }

    @Test
    fun `should throw  InvalidIdException when ids dont match`() {
        val expected = sampleEmployeeReceivable().copy(parentId = "loanId2")

        assertThrows(InvalidIdException::class.java) {
            loan.setReceivable(expected)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when type is advance`() {
        val expected = sampleEmployeeReceivable().copy(
            parentId = "loanId1",
            type = EmployeeReceivableTicket.ADVANCE
        )

        assertThrows(WrongMethodTypeException::class.java) {
            loan.setReceivable(expected)
        }

    }

    @Test
    fun `should throw WrongMethodTypeException when type is aid`() {
        val expected = sampleEmployeeReceivable().copy(
            parentId = "loanId1",
            type = EmployeeReceivableTicket.TRAVEL_AID
        )

        assertThrows(WrongMethodTypeException::class.java) {
            loan.setReceivable(expected)
        }

    }

    //---------------------------------------------------------------------------------------------//
    // toDto()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return an dto when call toDto`() {
        val expected = sampleLoanDto()

        val dto = loan.toDto()

        assertEquals(expected, dto)
    }

    //---------------------------------------------------------------------------------------------//
    // getInstallmentAverageValue()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return null when the receivable is not set`() {
        assertNull(loan.getInstallmentAverageValue())
    }

    @Test
    fun `should throw EmptyDataException when the transaction list is empty`() {
        loan.setReceivable(
            sampleEmployeeReceivable().copy(
                parentId = "loanId1",
                type = EmployeeReceivableTicket.LOAN
            )
        )

        assertThrows(EmptyDataException::class.java) {
            loan.alreadyPaidTransactions()
        }

    }

    @Test
    fun `should return the average value`() {
        val receivable = sampleEmployeeReceivable().copy(
            parentId = "loanId1",
            type = EmployeeReceivableTicket.LOAN
        )
        receivable.addAllTransactions(listOf(
            sampleTransaction().copy(parentId = "employeeReceivableId1", _isPaid = true),
            sampleTransaction().copy(parentId = "employeeReceivableId1", id = "transactionId2")
        ))
        loan.setReceivable(receivable)

        val expected = 1
        val actual = loan.alreadyPaidTransactions()

        assertEquals(expected, actual)
    }


}