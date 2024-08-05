package br.com.apps.model.model.finance

import br.com.apps.model.model.finance.receivable.EmployeeReceivable
import br.com.apps.model.test_cases.sampleEmployeeReceivable
import br.com.apps.model.test_cases.sampleEmployeeReceivableDto
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EmployeeReceivableTest {

    private lateinit var receivable: EmployeeReceivable

    @Before
    fun setup() {
        receivable = sampleEmployeeReceivable()
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