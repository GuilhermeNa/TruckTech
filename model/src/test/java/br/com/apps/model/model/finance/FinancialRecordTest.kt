package br.com.apps.model.model.finance

import br.com.apps.model.exceptions.DuplicatedItemsException
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.EmptyIdException
import br.com.apps.model.exceptions.FinancialRecordException
import br.com.apps.model.model.finance.payable.EmployeePayable
import br.com.apps.model.test_cases.sampleEmployeePayable
import br.com.apps.model.test_cases.sampleTransaction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FinancialRecordTest {

    private lateinit var payment: EmployeePayable
    private lateinit var transaction: Transaction

    @Before
    fun setup() {
        payment = sampleEmployeePayable()
        transaction = sampleTransaction()
    }

    //----------------------------------------------------------------------------------------------
    // addTransaction()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should add a transaction to the list`() {
        payment.addTransaction(transaction)

        val contains = payment.contains(transaction)

        assertTrue(contains)
    }

    @Test
    fun `should throw DuplicatedItemsException when adding a transaction with an already existing id`() {
        val transactionB = transaction.copy(number = 2)

        payment.addTransaction(transaction)

        assertThrows(DuplicatedItemsException::class.java) {
            payment.addTransaction(transactionB)
        }

    }

    @Test
    fun `should throw DuplicatedItemsException when adding a transaction with an already existing installment number`() {
        val transactionB = transaction.copy(id = "paymentId2")

        payment.addTransaction(transaction)

        assertThrows(DuplicatedItemsException::class.java) {
            payment.addTransaction(transactionB)
        }

    }

    //----------------------------------------------------------------------------------------------
    // clearTransactions()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should clear transactions list`() {
        val transactionB = transaction.copy(id = "transactionId2", number = 2)

        payment.addTransaction(transaction)
        payment.addTransaction(transactionB)
        payment.clearTransactions()

        assertEquals(0, payment.transactionsSize())
    }

    //----------------------------------------------------------------------------------------------
    // getTransactions()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should return a list ordered by its installment number`() {
        val transactionB = transaction.copy(id = "transactionId2", number = 2)
        val transactionC = transaction.copy(id = "transactionId3", number = 3)

        payment.addTransaction(transactionB)
        payment.addTransaction(transaction)
        payment.addTransaction(transactionC)

        val itemOnPos0 = payment.geTransactions()[0]
        val itemOnPos1 = payment.geTransactions()[1]
        val itemOnPos2 = payment.geTransactions()[2]

        assertEquals(transaction, itemOnPos0)
        assertEquals(transactionB, itemOnPos1)
        assertEquals(transactionC, itemOnPos2)

    }

    //----------------------------------------------------------------------------------------------
    // removeItem()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should remove an item from the list`() {
        payment.addTransaction(transaction)

        payment.removeItem(transaction.id)
        val size = payment.transactionsSize()

        assertEquals(0, size)
    }

    @Test
    fun `should not remove an item from the list when id is wrong`() {
        payment.addTransaction(transaction)

        payment.removeItem("wrongId")
        val size = payment.transactionsSize()

        assertEquals(1, size)
    }

    @Test
    fun `should throw exception when the id is empty`() {
        payment.addTransaction(transaction)

        assertThrows(EmptyIdException::class.java) {
            payment.removeItem("")
        }
    }

    //----------------------------------------------------------------------------------------------
    // validateTransactionsPayment()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should throw EmptyDataException when transactions list is empty`() {
        assertThrows(EmptyDataException::class.java) {
            payment.validateTransactionsPayment()
        }
    }

    @Test
    fun `should throw FinancialRecordException when there is an unpaid transaction`() {
        val unpaidTransaction = transaction.copy(_isPaid = false)
        payment.addTransaction(unpaidTransaction)

        assertThrows(FinancialRecordException::class.java) {
            payment.validateTransactionsPayment()
        }
    }

    @Test
    fun `should pass when all transactions are paid`() {
        val paidTransaction = transaction.copy(_isPaid = true)
        payment.addTransaction(paidTransaction)

        payment.validateTransactionsPayment() // Não deve lançar exceção
    }


}