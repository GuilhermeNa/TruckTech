package br.com.apps.model.model.finance

import br.com.apps.model.model.finance.payable.EmployeePayable
import br.com.apps.model.test_cases.sampleEmployeePayable
import br.com.apps.model.test_cases.sampleEmployeePayableDto
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EmployeePayableTest {

    private lateinit var payable: EmployeePayable

    @Before
    fun setup() {
        payable = sampleEmployeePayable()
    }

    //----------------------------------------------------------------------------------------------
    // toDto()
    //----------------------------------------------------------------------------------------------

    @Test
    fun `should return a dto object with correspondent data`() {
        val expected = sampleEmployeePayableDto()

        val dto = payable.toDto()

        assertEquals(expected, dto)
    }



}