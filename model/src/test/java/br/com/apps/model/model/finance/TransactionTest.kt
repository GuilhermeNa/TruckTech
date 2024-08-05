package br.com.apps.model.model.finance

import br.com.apps.model.test_cases.sampleTransaction
import br.com.apps.model.test_cases.sampleTransactionDto
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TransactionTest {

    private lateinit var transaction: Transaction

    @Before
    fun setup() {
        transaction = sampleTransaction()
    }

    //----------------------------------------------------------------------------------------------
    // toDto()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should return a dto object with correspondent data`() {
        val expectedDto = sampleTransactionDto()

        val dto = transaction.toDto()

        Assert.assertEquals(expectedDto, dto)
    }

    //----------------------------------------------------------------------------------------------
    // processTransaction()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should set _isPaid as true`() {
        transaction.processTransaction()
        assertTrue(transaction.isPaid)
    }

    //----------------------------------------------------------------------------------------------
    // undoTransaction()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should set _isPaid as false`() {
        transaction.undoTransaction()
        assertFalse(transaction.isPaid)
    }

}