package br.com.apps.model.enums

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.user.PermissionLevelType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PermissionLevelTypeTest {

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return type OPERATIONAL when param is string OPERATIONAL`() {
        val type = PermissionLevelType.getType("OPERATIONAL")
        assertEquals(PermissionLevelType.OPERATIONAL, type)
    }

    @Test
    fun `should return type TRAINEE when param is string TRAINEE`() {
        val type = PermissionLevelType.getType("TRAINEE")
        assertEquals(PermissionLevelType.TRAINEE, type)
    }

    @Test
    fun `should return type ADMIN_ASSISTANT when param is string ADMIN_ASSISTANT`() {
        val type = PermissionLevelType.getType("ADMIN_ASSISTANT")
        assertEquals(PermissionLevelType.ADMIN_ASSISTANT, type)
    }

    @Test
    fun `should return type MANAGER when param is string MANAGER`() {
        val type = PermissionLevelType.getType("MANAGER")
        assertEquals(PermissionLevelType.MANAGER, type)
    }

    @Test
    fun `should return type MASTER when param is string MASTER`() {
        val type = PermissionLevelType.getType("MASTER")
        assertEquals(PermissionLevelType.MASTER, type)
    }

    @Test
    fun `should throw InvalidTypeException when type is not registered`() {
        assertThrows(InvalidTypeException::class.java) {
            PermissionLevelType.getType("INVALID_TYPE")
        }
    }
}