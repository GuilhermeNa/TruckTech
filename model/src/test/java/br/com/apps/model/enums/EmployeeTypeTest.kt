package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.employee.EmployeeType
import org.junit.Assert
import org.junit.Test

class EmployeeTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type DRIVER when param is string DRIVER`() {
        val type = EmployeeType.getType("DRIVER")
        Assert.assertEquals(EmployeeType.DRIVER, type)
    }

    @Test
    fun `should return type ADMIN when param is string ADMIN`() {
        val type = EmployeeType.getType("ADMIN")
        Assert.assertEquals(EmployeeType.ADMIN, type)
    }

    @Test
    fun `should throw InvalidTypeException when type is not registered`() {
        Assert.assertThrows(InvalidTypeException::class.java) {
            EmployeeType.getType("")
        }
    }


}