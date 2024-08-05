package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RequestItemTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type REFUEL when param is string REFUEL`() {
        val type = RequestItemType.getType("REFUEL")
        assertEquals(RequestItemType.REFUEL, type)
    }

    @Test
    fun `should return type COST when param is string COST`() {
        val type = RequestItemType.getType("COST")
        assertEquals(RequestItemType.COST, type)
    }

    @Test
    fun `should return type WALLET when param is string WALLET`() {
        val type = RequestItemType.getType("WALLET")
        assertEquals(RequestItemType.WALLET, type)
    }

    @Test
    fun `should throw InvalidTypeException when type is not registered`() {
        assertThrows(InvalidTypeException::class.java) {
            RequestItemType.getType("INVALID_TYPE")
        }
    }

}