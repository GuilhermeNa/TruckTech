package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.payroll.AdvanceType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class AdvanceTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type COMMISSION when param is string COMMISSION`() {
        val type = AdvanceType.getType("COMMISSION")
        assertEquals(AdvanceType.COMMISSION, type)
    }

    @Test
    fun `should return type PAYROLL when param is string PAYROLL`() {
        val type = AdvanceType.getType("PAYROLL")
        assertEquals(AdvanceType.PAYROLL, type)
    }

    @Test
    fun `should throw InvalidTypeException when type is not registered`() {
        assertThrows(InvalidTypeException::class.java) {
            AdvanceType.getType("INVALID_TYPE")
        }
    }

}