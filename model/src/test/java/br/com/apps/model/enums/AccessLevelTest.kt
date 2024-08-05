package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.user.AccessLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class AccessLevelTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type OPERATIONAL when param is string OPERATIONAL`() {
        val type = AccessLevel.getType("OPERATIONAL")
        assertEquals(AccessLevel.OPERATIONAL, type)
    }

    @Test
    fun `should return type TRAINEE when param is string TRAINEE`() {
        val type = AccessLevel.getType("TRAINEE")
        assertEquals(AccessLevel.TRAINEE, type)
    }

    @Test
    fun `should return type ADMIN_ASSISTANT when param is string ADMIN_ASSISTANT`() {
        val type = AccessLevel.getType("ADMIN_ASSISTANT")
        assertEquals(AccessLevel.ADMIN_ASSISTANT, type)
    }

    @Test
    fun `should return type MANAGER when param is string MANAGER`() {
        val type = AccessLevel.getType("MANAGER")
        assertEquals(AccessLevel.MANAGER, type)
    }

    @Test
    fun `should return type MASTER when param is string MASTER`() {
        val type = AccessLevel.getType("MASTER")
        assertEquals(AccessLevel.MASTER, type)
    }

    @Test
    fun `should throw InvalidTypeException when type is not registered`() {
        assertThrows(InvalidTypeException::class.java) {
            AccessLevel.getType("INVALID_TYPE")
        }
    }
}