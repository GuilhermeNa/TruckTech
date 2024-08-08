package br.com.apps.model.model.finance

import br.com.apps.model.model.finance.receivable.EmployeeReceivable
import br.com.apps.model.model.finance.receivable.EmployeeReceivable.Companion.merge
import br.com.apps.model.test_cases.sampleEmployeeReceivable
import br.com.apps.model.test_cases.sampleEmployeeReceivableDto
import br.com.apps.model.test_cases.sampleTransaction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EmployeeReceivableTest {

    private lateinit var receivable: EmployeeReceivable

    @Before
    fun setup() {
        receivable = sampleEmployeeReceivable()
    }

    //---------------------------------------------------------------------------------------------//
    // merge()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun`should merge a receivable list and a transaction list`() {
        val receivables = listOf(
            sampleEmployeeReceivable(),
            sampleEmployeeReceivable().copy(id = "employeeReceivableId2")
        )
        val transactions = listOf(
            sampleTransaction().copy(parentId = "employeeReceivableId1"),
            sampleTransaction().copy(id= "transactionId2", parentId = "employeeReceivableId2")
        )
        receivables.merge(transactions)

        val expected = "transactionId1"
        val actual = receivables[0].geTransactions()[0].id
        assertEquals(expected, actual)

        val expectedB = "transactionId2"
        val actualB = receivables[1].geTransactions()[0].id
        assertEquals(expectedB, actualB)

    }

    //----------------------------------------------------------------------------------------------
    // toDto()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should return a dto object with correspondent data`() {
        val expectedDto = sampleEmployeeReceivableDto()

        val dto = receivable.toDto()

        assertEquals(expectedDto, dto)
    }

}